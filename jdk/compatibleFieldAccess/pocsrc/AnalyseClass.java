import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class AnalyseClass {

	public interface AnalyseField {

		boolean isProtectAccessable();

		boolean isPublicAccessable();

		boolean isPackageAccessable();

	}

	private String className;

	private ClassLoader loader;
	private ClassReader cr;
	private ClassNode cNode;

	public AnalyseClass(String className, ClassLoader loader) {
		this.className = className;
		this.loader = loader;
	}

	public AnalyseClass findInClassHirachie(String owner)
			throws ClassNotFoundException {
		if (className.equals(owner)) {
			return this;
		} else {
			AnalyseClass superClass = getSuperClass();
			return superClass == null ? null : superClass
					.findInClassHirachie(owner);
		}
	}

	private String getSuperClassName() throws ClassNotFoundException {
		return getClassReader().getSuperName();
	}

	private ClassReader getClassReader() throws ClassNotFoundException {
		if (cr == null) {
			try {
				cr = new ClassReader(
						loader.getResourceAsStream(getResourceNameOfClass()));
			} catch (IOException e) {
				throw new ClassNotFoundException("Could not analyse class: "
						+ className, e);
			}
		}
		return cr;
	}

	public AnalyseField findField(String name) throws ClassNotFoundException {
		for (FieldNode field : getClassNode().fields) {
			if (field.name.equals(name)) {
				final FieldNode fn = field;
				return new AnalyseField() {

					@Override
					public boolean isPublicAccessable() {
						return (fn.access & Opcodes.ACC_PUBLIC) != 0;
					}

					@Override
					public boolean isProtectAccessable() {
						return (fn.access & Opcodes.ACC_PROTECTED) != 0
								|| isPublicAccessable();
					}

					@Override
					public boolean isPackageAccessable() {
						return (fn.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) == 0
								|| isPublicAccessable();
					}

					@Override
					public String toString() {
						return "AnalyseField:" + fn.desc + " " + fn.name
								+ " in " + AnalyseClass.this.toString();
					}

				};
			}
		}
		return null;
	}

	private ClassNode getClassNode() throws ClassNotFoundException {
		if (cNode == null) {
			cNode = new ClassNode();
			getClassReader().accept(
					cNode,
					ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES
							| ClassReader.SKIP_DEBUG);
		}
		return cNode;
	}

	public AnalyseClass getSuperClass() throws ClassNotFoundException {
		String superClass = getSuperClassName();
		if (superClass != null) {
			return new AnalyseClass(superClass, loader);
		} else {
			return null;
		}
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return "AnalyseClass:" + className + "("
				+ loader.getResource(getResourceNameOfClass()) + ")";
	}

	private String getResourceNameOfClass() {
		return className + ".class";
	}

	public boolean accessorMethodExists(String name, boolean get, int modifier)
			throws ClassNotFoundException {

		ClassNode cNode = getClassNode();
		if (cNode.methods != null) {
			for (MethodNode m : cNode.methods) {
				if (m.visibleAnnotations != null) {
					for (AnnotationNode va : m.visibleAnnotations) {
						if (va.desc.equals("Ljavalang/ref/Accessor;")) {
							Map<Object, Object> values = new HashMap<>();
							for (int i = 0; va.values != null
									&& i < va.values.size() / 2; i++) {
								values.put(va.values.get(i * 2),
										va.values.get((i * 2) + 1));
							}
							String fieldName = (String) values.get("value");
							fieldName = AccessorMethodUtil
									.determineAccessedFieldname(get, m.name,
											fieldName);
							if (fieldName != null && fieldName.equals(name)) {
								int access = m.access & (Opcodes.ACC_PUBLIC|Opcodes.ACC_PRIVATE|Opcodes.ACC_PROTECTED|Opcodes.ACC_STATIC); 
								if (modifier == access) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
