package tbIncubator.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import tbIncubator.domain.DataType;
import tbIncubator.domain.DataTypeSimplification;
import tbIncubator.domain.HasParameters;
import tbIncubator.domain.HasRepresentatives;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Link.LinkType;
import tbIncubator.domain.Representative;
import tbIncubator.domain.SubCall;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import com.squareup.javapoet.TypeVariableName;

public class JavaCodeGeneratorPoet extends JavaCodeGenerator {

	private class MyFlushToDisk implements FlushToDir {

		private final Builder[] enu;
		private final String name;
		private final String packageName;
		private final String subDir;
		private final boolean[] enabled;
		private Runnable run;

		public MyFlushToDisk(String packageName, String name, String subDir,
				Builder... enu) {
			this.packageName = packageName;
			this.name = name;
			this.subDir = subDir;
			this.enu = enu;
			this.enabled = new boolean[enu.length];
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void flush(File dir) throws IOException {
			if (run != null) {
				run.run();
			}
			for (int i = 0; i < enabled.length; i++) {
				if (enabled[i]) {
					JavaFile.builder(packageName, enu[i].build()).build()
							.writeTo(new File(dir, subDir));
				}
			}
		}

		public void enableBuilder(int index) {
			enabled[index] = true;
		}

		public void disableBuilder(int index) {
			enabled[index] = false;
		}

		public void flushPreprocess(Runnable run) {
			this.run = run;
		}

	}

	private static final int INDEX_ENUM = 0;
	private static final int INDEX_INTERFACE = 0;
	private static final int INDEX_IMPL = 1;
	private static final int INDEX_TESTSATZ = 0;

	Map<String, MyFlushToDisk> interaktionenFlushes = new HashMap<String, MyFlushToDisk>();
	private Object dataTypeSimplification;
	private Map<MyFlushToDisk, Integer> numberOfMethodsWithSubCalls;
	private Map<MyFlushToDisk, Integer> numberOfMethods;
	private static final Set<String> VERBOTENE_FELD_NAME;

	static {
		Set<String> temp = new HashSet<>();
		temp.add("boolean");
		VERBOTENE_FELD_NAME = Collections.unmodifiableSet(temp);
	}

	public JavaCodeGeneratorPoet(List<DataType> datatypes,
			List<Interaction> interactions, List<TestSatz> testsaetze) {
		super(datatypes, interactions, testsaetze);
		this.numberOfMethodsWithSubCalls = new HashMap<MyFlushToDisk, Integer>();
		this.numberOfMethods = new HashMap<MyFlushToDisk, Integer>();
	}

	@Override
	protected FlushToDir generateDataType(DataType dataType) {

		String fqClassName = getFqClassName(dataType);
		DataTypeSimplification lookup = DataTypeSimplification
				.lookup(fqClassName);
		if (lookup != null) {
			return noFlushableResult(fqClassName);
		}

		if (shouldBeTransformed(dataType)) {
			Builder enu = TypeSpec.enumBuilder(dataType.getSimpleName());

			enu.addModifiers(Modifier.PUBLIC);

			addEnumConstants(dataType, enu);

			MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder();
			for (Link fie : dataType.fieldLinks) {
				DataType dt = lookupDataType(fie.ref);
				if (dt != null) {
					TypeName fiedClass = buildClassName(dt);
					String fiename = createFieldName(fie.name);
					FieldSpec fs = FieldSpec.builder(fiedClass, fiename,
							Modifier.PUBLIC, Modifier.FINAL).build();
					enu.addField(fs);
					addSimpleValueParameterToCtor(ctorBuilder, fiedClass,
							fiename);
				}
			}
			if (dataType.fieldLinks.isEmpty()) {
				enu.addField(FieldSpec.builder(String.class,
						"textuelleRepresentation", Modifier.PUBLIC,
						Modifier.FINAL).build());
				addSimpleValueParameterToCtor(ctorBuilder,
						TypeName.get(String.class), "textuelleRepresentation");
			}

			enu.addMethod(ctorBuilder.build());

			MyFlushToDisk mfd = new MyFlushToDisk(dataType.getPackage(),
					dataType.getSimpleName(), "datenTypen", enu);
			mfd.enableBuilder(INDEX_ENUM);
			return mfd;
		} else {
			return noFlushableResult(fqClassName);
		}
	}

	private void addEnumConstants(HasRepresentatives dataType, Builder enu) {
		boolean wasEingefuegt = false;
		for (Representative repre : dataType.getRepresentatives()) {
			wasEingefuegt = true;
			String javaName = repre.toJavaName();
			if (!javaName.trim().isEmpty()) {
				ArrayList<TypeName> tvn = new ArrayList<TypeName>(
						repre.representativeLinks.size() * 2);
				int index = 0;
				StringBuilder sb = new StringBuilder();
				for (Link ele : repre.representativeLinks) {
					Representative lookupRepresentative = lookupRepresentative(ele.ref);
					DataType dt = lookupRepresentative.getDefinedIn();

					ClassName className = buildClassName(dt);
					DataTypeSimplification dts = lookupTypeSimplification(dt);
					if (dts == null) {
						tvn.add(className);
						tvn.add(TypeVariableName.get(
								lookupRepresentative.toJavaName(), className));
						sb.append("$T.$L,");
					} else {
						sb.append(representativeSimplified("Static.",
								lookupRepresentative, dts));
					}
				}
				sb.setLength(Math.max(0, sb.length() - 1));
				if (repre.representativeLinks.size() > 0) {
					enu.addEnumConstant(
							javaName,
							TypeSpec.anonymousClassBuilder(sb.toString(),
									tvn.toArray()).build());
				} else {
					enu.addEnumConstant(javaName, TypeSpec
							.anonymousClassBuilder("$S", repre.name).build());
				}

			}
		}
		if (!wasEingefuegt) {
			enu.addEnumConstant("DEFAULT");
		}
	}

	private String createFieldName(String fieName) {
		String fiename = fieName.substring(0, 1).toLowerCase()
				+ fieName.substring(1);
		fiename = TbElement.replaceAll(fiename);
		if (VERBOTENE_FELD_NAME.contains(fiename)) {
			fiename = "_" + fiename;
		}
		return fiename;
	}

	private DataTypeSimplification lookupTypeSimplification(DataType dt) {
		String tName = dt.getPackage() + "." + dt.getSimpleName();
		DataTypeSimplification dts = DataTypeSimplification.lookup(tName);
		return dts;
	}

	private ClassName buildClassName(DataType dt) {
		ClassName className;
		DataTypeSimplification dts = lookupTypeSimplification(dt);
		if (dts == null) {
			className = ClassName.get(dt.getPackage(), dt.getSimpleName());
		} else {
			className = ClassName.get(dts.valueType);
		}
		return className;
	}

	private boolean shouldBeTransformed(TbElement element) {
		return !element.getPackage().startsWith("Schadensystem");
	}

	@Override
	protected FlushToDir generateInteraction(Interaction inter) {
		String pack = inter.getPackage();
		String fqClassName = getFQClassName(inter);
		String fqPackage = fqClassName.substring(0,
				fqClassName.lastIndexOf('.'));
		String className = fqClassName.substring(fqPackage.length() + 1);

		MyFlushToDisk mfd = interaktionenFlushes.get(fqClassName);
		if (mfd == null) {
			Builder interfaceBuilder = TypeSpec.interfaceBuilder("I"
					+ className);
			Builder implBuilder = TypeSpec.classBuilder(className);
			// implBuilder.addSuperinterface(ClassName.get(fqPackage, "I"
			// + className));
			interfaceBuilder.addAnnotation(inc.tf.Interaction.class);
			interfaceBuilder.addModifiers(Modifier.PUBLIC);
			implBuilder.addModifiers(Modifier.PUBLIC);
			implBuilder.superclass(ClassName.get("inc",
					"ZusammengesetzteInteraktion"));
			mfd = new MyFlushToDisk(fqPackage, className, "interaktionen",
					interfaceBuilder, implBuilder);
			mfd.enableBuilder(INDEX_INTERFACE);
			mfd.disableBuilder(INDEX_IMPL);
			final MyFlushToDisk fReference = mfd;

			mfd.flushPreprocess(new Runnable() {

				@Override
				public void run() {
					int numMethods = numberOfMethods.get(fReference);
					int numMethodsWithSubCalls = numberOfMethodsWithSubCalls
							.get(fReference);
					if (numMethodsWithSubCalls > 1 || numMethods == 1) {
						fReference.disableBuilder(INDEX_INTERFACE);
						fReference.enableBuilder(INDEX_IMPL);
					}

				}
			});

			numberOfMethodsWithSubCalls.put(mfd, 0);
			numberOfMethods.put(mfd, 0);

			interaktionenFlushes.put(fqClassName, mfd);
		}

		com.squareup.javapoet.MethodSpec.Builder mBuilder = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.ABSTRACT);
		com.squareup.javapoet.MethodSpec.Builder mBuilderImpl = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.STATIC);

		configureInteractionMethodSpec(inter, mBuilder, mBuilderImpl);
		MethodSpec methodSpec = mBuilder.build();

		mfd.enu[0].addMethod(methodSpec);

		increase(numberOfMethods, mfd);

		if (inter.getSubCalls().iterator().hasNext()) {
			increase(numberOfMethodsWithSubCalls, mfd);
		}

		buildMethodImplementation(inter, fqClassName, mBuilderImpl, false);
		mfd.enu[1].addMethod(mBuilderImpl.build());

		return mfd;
	}

	private void increase(Map<MyFlushToDisk, Integer> inMap,
			MyFlushToDisk position) {
		inMap.put(position, inMap.get(position) + 1);
	}

	private void buildMethodImplementation(HasSubCalls hasSubCalls,
			String fqClassName,
			com.squareup.javapoet.MethodSpec.Builder mBuilderImpl,
			boolean testMethod) {
		for (SubCall subCall : hasSubCalls.getSubCalls()) {
			Interaction lookupInteraction = lookupInteraction(subCall.interactionRef);

			StringBuilder sb = new StringBuilder();
			if (lookupInteraction != null) {
				String callClass = getFQClassName(lookupInteraction);
				String callClassPackage = callClass.substring(0,
						callClass.lastIndexOf('.'));
				String callClassName = callClass.substring(callClassPackage
						.length() + 1);
				if (!callClass.equals(fqClassName)) {
					sb.append(callClassName);
					sb.append(".");
				}
				sb.append(lookupInteraction.getSimpleName());
				sb.append("(");
				for (Link para : subCall.parameters) {
					if (para.type == LinkType.REPRESENTATIVE) {
						Representative repre = lookupRepresentative(para.ref);
						DataType definedIn = repre.getDefinedIn();
						DataTypeSimplification dts = lookupTypeSimplification(definedIn);
						if (dts == null) {
							// sb.append(definedIn.getPackage());
							// sb.append(".");
							sb.append(definedIn.getSimpleName());
							sb.append(".");
							sb.append(repre.toJavaName());
							sb.append(",");
						} else {
							sb.append(representativeSimplified("", repre, dts));
						}
					} else if (para.type == LinkType.PARAMETER) {
						if (testMethod) {
							sb.append("parameter.");
						}
						InteractionParameter iPara = lookupInteractionParameter(para.ref);
						if (iPara != null) {
							sb.append(iPara.getJavaName());
						} else {
							sb.append("FIXME:" + para.ref);
						}
						sb.append(",");

					}
				}
				if (!subCall.parameters.isEmpty()) {
					sb.setLength(sb.length() - 1);
				}
				sb.append(")");
				mBuilderImpl.addStatement(sb.toString());
			}
		}
	}

	private String representativeSimplified(String prefix,
			Representative repre, DataTypeSimplification dts) {
		StringBuilder sb = new StringBuilder();
		if (dts.valueType.equals(String.class)) {
			if (repre.name.equals("<leer>") || repre.name.equals("<<leer>>")
					|| repre.name.equals("<keine Auswahl>")) {
				sb.append("null,");
			} else {
				sb.append("\"");
				sb.append(repre.name);
				sb.append("\",");
				if (repre.name.contains("<")) {
					System.out.println(repre.name);
				}
			}
		} else {
			sb.append(prefix);
			sb.append(dts.methodName);
			sb.append("(\"");
			sb.append(repre.name);
			sb.append("\"),");
		}
		return sb.toString();
	}

	private void configureTestMethodSpec(HasRepresentatives inter,
			com.squareup.javapoet.MethodSpec.Builder mBuilder) {
		mBuilder.addModifiers(Modifier.PUBLIC);
	}

	private void configureInteractionMethodSpec(HasParameters inter,
			com.squareup.javapoet.MethodSpec.Builder... mBuilders) {
		for (com.squareup.javapoet.MethodSpec.Builder builder : mBuilders) {
			builder.addModifiers(Modifier.PUBLIC);
		}
		for (InteractionParameter ele : inter.getParameters()) {
			DataType dt = lookupDataType(ele.dataTypeRef.ref);
			if (dt != null) {
				DataTypeSimplification dts = lookupTypeSimplification(dt);
				ParameterSpec para;
				if (dts == null) {
					ClassName cn = buildClassName(dt);
					para = ParameterSpec.builder(cn, ele.getJavaName()).build();
				} else {
					para = ParameterSpec.builder(dts.valueType,
							ele.getJavaName()).build();
				}
				for (com.squareup.javapoet.MethodSpec.Builder builder : mBuilders) {
					builder.addParameter(para);
				}
			}
		}
	}

	@Override
	protected FlushToDir generateTest(TestSatz test) {
		String pack = test.getPackage();
		String fqClassName = getFQClassName(test);
		String fqPackage = fqClassName.substring(0,
				fqClassName.lastIndexOf('.'));
		String className = fqClassName.substring(fqPackage.length() + 1);
		className = className.replace('.', '_');

		if (shouldTestBeIgnored(fqPackage)) {
			return noFlushableResult(className);
		}
		boolean hasParameters = test.getParameterValues().iterator().hasNext();

		Builder enu = TypeSpec.classBuilder(className);
		if (hasParameters) {
			com.squareup.javapoet.AnnotationSpec.Builder annoBuilder = AnnotationSpec
					.builder(RunWith.class);
			annoBuilder.addMember("value", "Parameterized.class");
			enu.addAnnotation(annoBuilder.build());
			ClassName rawType = ClassName.get("inc", "BaseTestWithData");
			ClassName type = ClassName.get("", className + ".Data");
			ParameterizedTypeName pType = ParameterizedTypeName.get(rawType,
					type);
			enu.superclass(pType);
			com.squareup.javapoet.MethodSpec.Builder ctor = MethodSpec
					.constructorBuilder();
			ctor.addParameter(ClassName.get("", "Data"), "parameters");
			ctor.addCode("super(parameters);");
			ctor.addModifiers(Modifier.PUBLIC);
			enu.addMethod(ctor.build());

			// @Parameters(name = "{0})")
			// public static Iterable<Object[]> parameters() {
			// return BaseTestWithData.parametersHelper(Data.class);
			// }

			com.squareup.javapoet.MethodSpec.Builder methodBuilder = MethodSpec
					.methodBuilder("parameters");
			methodBuilder.addModifiers(Modifier.STATIC, Modifier.PUBLIC);
			methodBuilder.addAnnotation(AnnotationSpec
					.builder(Parameters.class).addMember("name", "\"{0}\"")
					.build());
			methodBuilder.addCode("BaseTestWithData.parametersHelper(Data.class, args);");
			enu.addMethod(methodBuilder.build());

		} else {
			enu.superclass(ClassName.get("inc", "BaseTest"));
		}
		enu.addModifiers(Modifier.PUBLIC);

		com.squareup.javapoet.MethodSpec.Builder methodBuilder = MethodSpec
				.methodBuilder("test");

		methodBuilder.addAnnotation(Test.class);
		if (hasParameters) {
			if (test.parameterAnzahlPasst()) {
				addTestDataEnum(enu, test.getParameters(), test);
			} else {
				methodBuilder.addAnnotation(ClassName.get("inc",
						"EtwasPasstNichtMitDenParametern"));
			}
		}

		configureTestMethodSpec(test, methodBuilder);
		buildMethodImplementation(test, fqClassName, methodBuilder, true);

		enu.addMethod(methodBuilder.build());

		MyFlushToDisk mfd = new MyFlushToDisk(fqPackage, className, "tests",
				enu);
		mfd.enableBuilder(INDEX_TESTSATZ);
		return mfd;
	}

	private void addTestDataEnum(Builder enu,
			Iterable<InteractionParameter> parameters,
			HasRepresentatives testfaelle) {
		Builder enumBuilder = TypeSpec.enumBuilder("Data");
		enumBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

		addEnumConstants(testfaelle, enumBuilder);

		MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder();
		for (InteractionParameter interactionParameter : parameters) {
			DataType dt = lookupDataType(interactionParameter.dataTypeRef.ref);
			ClassName buildClassName = buildClassName(dt);
			String fiename = createFieldName(interactionParameter.name);
			enumBuilder.addField(//
					FieldSpec.builder(buildClassName, fiename, Modifier.PUBLIC,
							Modifier.FINAL).build());

			addSimpleValueParameterToCtor(ctorBuilder, buildClassName, fiename);
		}
		enumBuilder.addMethod(ctorBuilder.build());
		enu.addType(enumBuilder.build());
	}

	private void addSimpleValueParameterToCtor(MethodSpec.Builder ctorBuilder,
			TypeName buildClassName, String fiename) {
		ctorBuilder.addParameter(ParameterSpec.builder(buildClassName, fiename)
				.build());
		ctorBuilder.addCode("this." + fiename + " = " + fiename + ";\n");
	}

	private boolean shouldTestBeIgnored(String fqPackage) {
		return fqPackage.toLowerCase().contains("manuelle_test");
	}

}
