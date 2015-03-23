package tbIncubator.generator;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import tbIncubator.domain.DataType;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Representative;
import tbIncubator.domain.SubCall;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.Link.LinkType;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
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

		public final Builder[] enu;
		public final String name;
		public final String packageName;

		public MyFlushToDisk(String packageName, String name, Builder... enu) {
			this.packageName = packageName;
			this.name = name;
			this.enu = enu;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void flush(File dir) throws IOException {
			for (Builder builder : enu) {
				JavaFile.builder(packageName, builder.build()).build()
						.writeTo(dir);
			}
		}

	}

	Map<String, MyFlushToDisk> interaktionenFlushes = new HashMap<String, MyFlushToDisk>();

	public JavaCodeGeneratorPoet(List datatypes, List interactions) {
		super(datatypes, interactions);
	}

	@Override
	protected FlushToDir generateDataType(DataType dataType) {
		String fqClassName = dataType.getPackage() + "."
				+ dataType.getSimpleName();
		if (dataType.getPackage().startsWith("Neues_Schadensystem")) {
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

			return new MyFlushToDisk(dataType.getPackage(),
					dataType.getSimpleName(), enu);
		} else {
			return noFlushableResult(fqClassName);
		}
	}

	@Override
	protected FlushToDir generateInteraction(Interaction inter) {
		String pack = inter.getPackage();
		String fqClassName = getFQClass(pack);
		String fqPackage = fqClassName.substring(0,
				fqClassName.lastIndexOf('.'));
		String className = fqClassName.substring(fqPackage.length() + 1);

		MyFlushToDisk mfd = interaktionenFlushes.get(fqClassName);
		if (mfd == null) {
			Builder interfaceBuilder = TypeSpec.interfaceBuilder(className);
			Builder implBuilder = TypeSpec.classBuilder(className + "Impl");
			implBuilder.addSuperinterface(ClassName.get(fqPackage, className));
			mfd = new MyFlushToDisk(fqPackage, className, interfaceBuilder,
					implBuilder);
			interaktionenFlushes.put(fqClassName, mfd);
		}

		com.squareup.javapoet.MethodSpec.Builder mBuilder = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.ABSTRACT);
		com.squareup.javapoet.MethodSpec.Builder mBuilderImpl = MethodSpec
				.methodBuilder(inter.getSimpleName()).addModifiers(
						Modifier.PUBLIC);
		for (InteractionParameter ele : inter.parameters) {
			DataType dt = lookupDataType(ele.dataTypeRef.ref);
			if (dt != null) {
				ClassName cn = ClassName.get(dt.getPackage(),
						dt.getSimpleName());
				ParameterSpec para = ParameterSpec.builder(cn,
						ele.getJavaName()).build();
				mBuilder.addParameter(para);
				mBuilderImpl.addParameter(para);
			}
		}
		MethodSpec methodSpec = mBuilder.build();

		mfd.enu[0].addMethod(methodSpec);

		com.squareup.javapoet.CodeBlock.Builder cBuilder = CodeBlock.builder();
		for (SubCall subCall : inter.subCalls) {
			Interaction lookupInteraction = lookupInteraction(subCall.interactionRef);

			StringBuilder sb = new StringBuilder();
			if (lookupInteraction != null) {
				String callClass = getFQClass(lookupInteraction.getPackage());
				if (!callClass.equals(fqClassName)) {
					sb.append(callClass);
					sb.append(".");
				}
				sb.append(lookupInteraction.getSimpleName());
				sb.append("(");
				for (Link para : subCall.parameters) {
					if (para.type == LinkType.REPRESENTATIVE) {
						Representative repre = lookupRepresentative(para.ref);
						DataType definedIn = repre.getDefinedIn();
//						sb.append(definedIn.getPackage());
//						sb.append(".");
						sb.append(definedIn.getSimpleName());
						sb.append(".");
						sb.append(repre.toJavaName());
						sb.append(",");
					} else if (para.type == LinkType.PARAMETER) {
						InteractionParameter iPara = lookupInteractionParameter(para.ref);
						if (iPara != null) {
							sb.append(iPara.getJavaName());
						}else {
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
		mfd.enu[1].addMethod(mBuilderImpl.build());

		return mfd;
	}

	@Override
	protected FlushToDir generateTest(Object test) {
		return noFlushableResult("");
	}

}
