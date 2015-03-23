package tbIncubator;

import java.io.File;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

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

		public final Builder enu;
		public final String name;
		public final String packageName;

		public MyFlushToDisk(String packageName, String name, Builder enu) {
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
			JavaFile.builder(packageName, enu.build()).build().writeTo(dir);
		}

	}

	Map<String, MyFlushToDisk> interaktionenFlushes = new HashMap<String, MyFlushToDisk>();

	protected JavaCodeGeneratorPoet(List datatypes, List interactions) {
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
								dt.name);
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

			MethodSpec.Builder ctorBuilder = MethodSpec.constructorBuilder();
			for (Link fie : dataType.fieldLinks) {
				DataType dt = lookupDataType(fie.ref);
				TypeName fiedClass = ClassName.get(dt.getPackage(),
						dt.getSimpleName());
				String fiename = fie.name.substring(0, 1).toLowerCase()
						+ fie.name.substring(1);
				FieldSpec fs = FieldSpec.builder(fiedClass, fiename,
						Modifier.PUBLIC, Modifier.FINAL).build();
				enu.addField(fs);
				ctorBuilder.addParameter(ParameterSpec.builder(fiedClass,
						fiename).build());
				ctorBuilder
						.addCode("this." + fiename + " = " + fiename + ";\n");
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
			mfd = new MyFlushToDisk(pack, className, interfaceBuilder);
			interaktionenFlushes.put(fqClassName, mfd);
		}

		com.squareup.javapoet.MethodSpec.Builder mBuilder = MethodSpec
				.methodBuilder(inter.getSimpleName())
				.addModifiers(Modifier.PUBLIC).addModifiers(Modifier.ABSTRACT);
		for (InteractionParameter ele : inter.parameters) {
			DataType dt = lookupDataType(ele.dataTypeRef.ref);
			ClassName cn = ClassName.get(dt.getPackage(), dt.getSimpleName());
			ParameterSpec para = ParameterSpec.builder(cn, ele.getJavaName()).build();
			mBuilder.addParameter(para);
		}
		MethodSpec methodSpec = mBuilder.build();

		mfd.enu.addMethod(methodSpec);

		return mfd;
	}

	@Override
	protected FlushToDir generateTest(Object test) {
		return noFlushableResult("");
	}

}
