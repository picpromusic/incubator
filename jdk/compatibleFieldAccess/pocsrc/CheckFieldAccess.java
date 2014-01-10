import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CheckFieldAccess {

	private static final int NON_MODIFIER = 0x0;
	private final ClassNode classNode;
	private static final Handle BOOTSTRAP_GET;
	private static final Handle BOOTSTRAP_SET;
	private ClassLoader loader;
	private static final boolean DISABLE_BCI;

	static {
		BOOTSTRAP_GET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"Bootstrapper",
				"getFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;");

		BOOTSTRAP_SET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"Bootstrapper",
				"setFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;");

		DISABLE_BCI = Boolean.getBoolean("disable-bci");
	}

	public CheckFieldAccess(ClassNode classNode, ClassLoader loader) {
		this.classNode = classNode;
		this.loader = loader;
	}

	public void makeItSo() {
		if (!DISABLE_BCI) {
			for (MethodNode method : (List<MethodNode>) classNode.methods) {
				checkFieldAccess(method);
			}
		}
	}

	private void checkFieldAccess(MethodNode method) {
		InsnList instructions = method.instructions;
		ListIterator<AbstractInsnNode> iterator = instructions.iterator();
		while (iterator.hasNext()) {
			AbstractInsnNode next = iterator.next();
			if (next.getOpcode() == Opcodes.GETFIELD) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins))
					continue;
				AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(//
						fins.name,//
						"(L" + fins.owner + ";)" + fins.desc,//
						BOOTSTRAP_GET,//
						fins.owner.replace('/', '.'), //
						NON_MODIFIER);
				iterator.set(getInsnNode);
			} else if (next.getOpcode() == Opcodes.PUTFIELD) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins))
					continue;
				AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(//
						fins.name,//
						"(L" + fins.owner + ";" + fins.desc + ")V",//
						BOOTSTRAP_SET,//
						fins.owner.replace('/', '.'), //
						NON_MODIFIER);
				iterator.set(getInsnNode);

			} else if (next.getOpcode() == Opcodes.GETSTATIC) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins))
					continue;
				AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(//
						fins.name,//
						"()" + fins.desc,//
						BOOTSTRAP_GET,//
						fins.owner.replace('/', '.'), //
						Modifier.STATIC);
				iterator.set(getInsnNode);
			} else if (next.getOpcode() == Opcodes.PUTSTATIC) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins))
					continue;
				AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(//
						fins.name,//
						"(" + fins.desc + ")V",//
						BOOTSTRAP_SET,//
						fins.owner.replace('/', '.'),//
						Modifier.STATIC);
				iterator.set(getInsnNode);

			}
		}
	}

	private boolean isAccessable(FieldInsnNode fins) {
		try {
			if (fins.owner.equals(this.classNode.name)) {
				for (FieldNode fieldNode : classNode.fields) {
					if (fieldNode.name.equals(fins.name)) {
						return true;
					}
				}
			}

			AnalyseClass ac = new AnalyseClass(classNode.name, loader);
			ac = ac.findInClassHirachie(fins.owner);
			boolean foundInHierachie = ac != null;
			if (!foundInHierachie) {
				ac = new AnalyseClass(fins.owner, loader);
			}
			AnalyseClass.AnalyseField af;
			boolean accessable = false;
			do {
				af = ac.findField(fins.name);
				String packageName = getPackage(ac.getClassName());
				ac = ac.getSuperClass();
				if (af != null) {
					accessable |= foundInHierachie ? af.isProtectAccessable()
							: af.isPublicAccessable();
					accessable |= getPackage(classNode.name).equals(packageName) && af.isPackageAccessable();
				}

			} while (!(accessable || ac == null));

			return accessable;

		} catch (SecurityException e) {
		} catch (ClassNotFoundException e) {
			return false;
		}
		return false;
	}

	private String getPackage(String name) {
		return name.substring(0,name.lastIndexOf('/'));
	}

}