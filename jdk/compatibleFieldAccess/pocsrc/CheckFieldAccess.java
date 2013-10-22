import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CheckFieldAccess {

	private final ClassNode classNode;
	private static final Handle BOOTSTRAP_GET;
	private static final Handle BOOTSTRAP_SET;

	static {
		BOOTSTRAP_GET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"Bootstrapper",
				"getFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;");

		BOOTSTRAP_SET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"Bootstrapper",
				"setFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;");
	}

	public CheckFieldAccess(ClassNode classNode) {
		this.classNode = classNode;
	}

	public void makeItSo() {
		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			checkFieldAccess(method);
			// method.maxStack += 7;
		}
	}

	private void checkFieldAccess(MethodNode method) {
		InsnList instructions = method.instructions;
		ListIterator<AbstractInsnNode> iterator = instructions.iterator();
		while (iterator.hasNext()) {
			AbstractInsnNode next = iterator.next();
			if (next.getOpcode() == Opcodes.GETFIELD) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins)) continue;
				if (!fins.owner.equals(classNode.name)) {
					AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(
							fins.name, "(L" + fins.owner + ";)" + fins.desc,
							BOOTSTRAP_GET);
					iterator.set(getInsnNode);
				}
			} else if (next.getOpcode() == Opcodes.PUTFIELD) {
				FieldInsnNode fins = (FieldInsnNode) next;
				if (isAccessable(fins)) continue;
				if (!fins.owner.equals(classNode.name)) {
					AbstractInsnNode getInsnNode = new InvokeDynamicInsnNode(
							fins.name, "(L" + fins.owner + ";" + fins.desc
									+ ")V", BOOTSTRAP_SET);
					iterator.set(getInsnNode);
				}
			}
		}
	}

	private boolean isAccessable(FieldInsnNode fins) {
		try {
			Class<?> clazz = Class.forName(fins.owner);
			Field field = clazz.getField(fins.name);
			if (Modifier.isPublic(field.getModifiers())
					|| fins.owner.equals(classNode.name)) {
				return true;
			}
		} catch (ClassNotFoundException e) {
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		return false;
	}
}
