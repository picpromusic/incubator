import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

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

	public AnalyseClass findInClassHirachie(String owner) throws ClassNotFoundException {
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
				throw new ClassNotFoundException("Could not analyse class: "+ className,e);
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
}
