package tbIncubator.generator;

import inc.BaseTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.junit.Test;

import tbIncubator.domain.DataType;
import tbIncubator.domain.HasParameters;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Link.LinkType;
import tbIncubator.domain.Representative;
import tbIncubator.domain.SubCall;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
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
	private Map<MyFlushToDisk,Integer> numberOfMethodsWithSubCalls;
	private Map<MyFlushToDisk,Integer> numberOfMethods;

	public JavaCodeGeneratorPoet(List<DataType> datatypes,
			List<Interaction> interactions, List<TestSatz> testsaetze) {
		super(datatypes, interactions, testsaetze);
		this.numberOfMethodsWithSubCalls = new HashMap<MyFlushToDisk,Integer>();
		this.numberOfMethods = new HashMap<MyFlushToDisk,Integer>();
	}

	@Override
	protected FlushToDir generateDataType(DataType dataType) {
		
		String fqClassName = getFqClassName(dataType);
		if (shouldBeTransformed(dataType)) {
			Builder enu = TypeSpec.enumBuilder(dataType.getSimpleName());
			enu.addModifiers(Modifier.PUBLIC);

			for (Representative repre : dataType.representatives) {
				String javaName = repre.toJavaName();
				if (!javaName.trim().isEmpty()) {
					TypeName tvn[] = new TypeName[repre.representativeLinks
							.size() * 2];
					int index = 0;
					StringBuilder sb = new StringBuilder();
					for (Link ele : repre.representativeLinks) {
						Representative lookupRepresentative = lookupRepresentative(ele.ref);
						DataType dt = lookupRepresentative.getDefinedIn();
						ClassName className = ClassName.get(dt.getPackage(),
								dt.getSimpleName());
						tvn[index++] = className;
						tvn[index++] = TypeVariableName.get(
								lookupRepresentative.toJavaName(), className);
						sb.append("$T.$L,");
					}
					sb.setLength(Math.max(0, sb.length() - 1));
					if (tvn.length > 0) {
						enu.addEnumConstant(javaName, TypeSpec
								.anonymousClassBuilder(sb.toString(), tvn)
								.build());
					} else {
						enu.addEnumConstant(javaName, TypeSpec
								.anonymousClassBuilder("$S", repre.name)
								.build());
					}

				}
			}
			if (dataType.representatives.isEmpty()) {
				enu.addEnumConstant("DEFAULT");
			}

			MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder();
			for (Link fie : dataType.fieldLinks) {
				DataType dt = lookupDataType(fie.ref);
				if (dt != null) {
					TypeName fiedClass = ClassName.get(dt.getPackage(),
							dt.getSimpleName());

					String fiename = fie.name.substring(0, 1).toLowerCase()
							+ fie.name.substring(1);
					fiename = TbElement.replaceAll(fiename);
					FieldSpec fs = FieldSpec.builder(fiedClass, fiename,
							Modifier.PUBLIC, Modifier.FINAL).build();
					enu.addField(fs);
					ctorBuilder.addParameter(ParameterSpec.builder(fiedClass,
							fiename).build());
					ctorBuilder.addCode("this." + fiename + " = " + fiename
							+ ";\n");
				}
			}
			if (dataType.fieldLinks.isEmpty()) {
				enu.addField(FieldSpec.builder(String.class,
						"textuelleRepresentation", Modifier.PUBLIC,
						Modifier.FINAL).build());
				ctorBuilder.addParameter(ParameterSpec.builder(String.class,
						"textuelleRepresentation").build());
				ctorBuilder
						.addCode("this.textuelleRepresentation = textuelleRepresentation;\n");
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
					int numMethodsWithSubCalls = numberOfMethodsWithSubCalls.get(fReference);
					if (numMethodsWithSubCalls > 1 || numMethods == 1) {
						fReference.disableBuilder(INDEX_INTERFACE);
						fReference.enableBuilder(INDEX_IMPL);
					}
					
				}
			});

			numberOfMethodsWithSubCalls.put(mfd,0);
			numberOfMethods.put(mfd,0);
			
			interaktionenFlushes.put(fqClassName, mfd);
		}

		com.squareup.javapoet.MethodSpec.Builder mBuilder = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.ABSTRACT);
		com.squareup.javapoet.MethodSpec.Builder mBuilderImpl = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.STATIC);
		
		
		configureMethodSpec(inter, mBuilder, mBuilderImpl);
		MethodSpec methodSpec = mBuilder.build();

		mfd.enu[0].addMethod(methodSpec);

		increase(numberOfMethods, mfd);
		
		if (inter.getSubCalls().iterator().hasNext()) {
			increase(numberOfMethodsWithSubCalls,mfd);
		}

		buildMethodImplementation(inter, fqClassName, mBuilderImpl);
		mfd.enu[1].addMethod(mBuilderImpl.build());

		return mfd;
	}

	private void increase(Map<MyFlushToDisk, Integer> inMap, MyFlushToDisk position) {
		inMap.put(position, inMap.get(position)+1);
	}

	private void buildMethodImplementation(HasSubCalls hasSubCalls, String fqClassName,
			com.squareup.javapoet.MethodSpec.Builder mBuilderImpl) {
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
						// sb.append(definedIn.getPackage());
						// sb.append(".");
						sb.append(definedIn.getSimpleName());
						sb.append(".");
						sb.append(repre.toJavaName());
						sb.append(",");
					} else if (para.type == LinkType.PARAMETER) {
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

	private void configureMethodSpec(HasParameters inter,
			com.squareup.javapoet.MethodSpec.Builder... mBuilders) {
		for (InteractionParameter ele : inter.getParameters()) {
			DataType dt = lookupDataType(ele.dataTypeRef.ref);
			if (dt != null) {
				ClassName cn = ClassName.get(dt.getPackage(),
						dt.getSimpleName());
				ParameterSpec para = ParameterSpec.builder(cn,
						ele.getJavaName()).build();
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

		Builder enu = TypeSpec.classBuilder(className);
		enu.superclass(BaseTest.class);
		enu.addModifiers(Modifier.PUBLIC);
		
		com.squareup.javapoet.MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("test");
		
		methodBuilder.addAnnotation(Test.class);
		configureMethodSpec(test, methodBuilder);
		buildMethodImplementation(test, fqClassName, methodBuilder);
		
		enu.addMethod(methodBuilder.build());

		MyFlushToDisk mfd = new MyFlushToDisk(fqPackage, className, "tests",
				enu);
		mfd.enableBuilder(INDEX_TESTSATZ);
		return mfd;
	}

	
}
