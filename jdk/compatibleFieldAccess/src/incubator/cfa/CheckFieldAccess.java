package incubator.cfa;

import java.io.PrintStream;
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
import org.objectweb.asm.tree.MethodNode;

public class CheckFieldAccess {

	private static final int NON_MODIFIER = 0x0;
	private final ClassNode classNode;
	private boolean sol1;
	private boolean sol2;
	private boolean solBoot;
	private int traceLevel;
	private PrintStream tracer;
	private static final Handle BOOTSTRAP_GET;
	private static final Handle BOOTSTRAP_SET;

	static {
		BOOTSTRAP_GET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"incubator/cfa/Bootstrapper",
				"getFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;");

		BOOTSTRAP_SET = new Handle(
				Opcodes.H_INVOKESTATIC,
				"incubator/cfa/Bootstrapper",
				"setFunction",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;");
	}

	public CheckFieldAccess(ClassNode classNode) {
		this.classNode = classNode;
	}

	public void makeItSo() {
		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			checkFieldAccess(method);
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
				return true;
			}
			Class<?> clazz = Class.forName(fins.owner);
			Field field = clazz.getField(fins.name);
			// Public check isn't enough, but should work here for demonstation.
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

	public void setSolution1(boolean enabled) {
		this.sol1 = enabled;
	}

	public void setSolution2(boolean enabled) {
		this.sol2 = enabled;
	}

	public void setSolutionBootstrap(boolean enabled) {
		this.solBoot = enabled;
	}

	public void setTraceOutput(PrintStream tracer) {
		this.tracer = tracer;
	}

	public void setTraceLevel(int traceLevel) {
		this.traceLevel = traceLevel;
	}
}
