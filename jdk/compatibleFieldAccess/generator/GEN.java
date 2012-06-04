import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GEN {

	public static void main(String[] args) throws IOException,
			NoSuchMethodException, SecurityException {
		genClass("TestOld", "OLD");
		genClass("TestNew", "NEW");
		genClass("TestNew2", "NEW2");
	}

	private static void genClass(String className, String testAgainst)
			throws FileNotFoundException, IOException, NoSuchMethodException,
			SecurityException {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES
				| ClassWriter.COMPUTE_MAXS);
		cw.visit(51, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object",
				null);
		cw.visitSource(className + ".txt", "");
		genNonStaticTest(testAgainst, cw);
		genStaticTest(testAgainst, cw);
		cw.visitEnd();

		byte[] byteArray = cw.toByteArray();
		System.out.println(byteArray.length);
		FileOutputStream fout = new FileOutputStream("gen/" + className
				+ ".class");
		fout.write(byteArray);
		fout.close();
	}

	private static void genStaticTest(String testAgainst, ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC
				| Opcodes.ACC_STATIC, "testItStatically", "()V", null, null);
		mw.visitCode();
		mw.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		// PS
		mw.visitInsn(Opcodes.DUP);
		// PS PS 
		mw.visitFieldInsn(Opcodes.GETSTATIC, testAgainst, "staticField", "Ljava/lang/Object;");
		// PS PS FIELD
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// PS 
		mw.visitTypeInsn(Opcodes.NEW, "java/lang/Object");
		// PS NEW_VALUE
		mw.visitInsn(Opcodes.DUP);
		// PS NEW_VALUE NEW_VALUE
		mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
				"<init>", "()V");
		// PS NEW_VALUE
		mw.visitFieldInsn(Opcodes.PUTSTATIC, testAgainst, "staticField", "Ljava/lang/Object;");
		// PS
		mw.visitFieldInsn(Opcodes.GETSTATIC, testAgainst, "staticField", "Ljava/lang/Object;");
		// PS NEW_VALUE
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// <>
		mw.visitInsn(Opcodes.RETURN);
		mw.visitMaxs(0, 0);
		mw.visitEnd();
		
	}

	private static void genNonStaticTest(String testAgainst, ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC
				| Opcodes.ACC_STATIC, "testIt", "()V", null, null);
		mw.visitCode();
		// mw.visitMethodInsn(Opcodes.INVOKESTATIC, "M", "cafeBabe", "()V");
		mw.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		// PS
		mw.visitTypeInsn(Opcodes.NEW, testAgainst);
		// PS TEST
		mw.visitInsn(Opcodes.DUP2);
		// PS TEST PS TEST
		mw.visitInsn(Opcodes.DUP);
		// PS TEST PS TEST TEST
		mw.visitMethodInsn(Opcodes.INVOKESPECIAL, testAgainst, "<init>", "()V");
		// PS TEST PS TEST
		mw.visitLineNumber(1, new Label());
		mw.visitFieldInsn(Opcodes.GETFIELD, testAgainst, "cause",
				"Ljava/lang/Throwable;");
		// PS TEST PS CAUSE
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// PS TEST
		mw.visitInsn(Opcodes.DUP);
		// PS TEST TEST
		mw.visitTypeInsn(Opcodes.NEW, "java/lang/RuntimeException");
		// PS TEST TEST RTE
		mw.visitInsn(Opcodes.DUP);
		// PS TEST TEST RTE RTE
		mw.visitLdcInsn("NEW");
		// PS TEST TEST RTE RTE STRING
		mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException",
				"<init>", "(Ljava/lang/String;)V");
		// PS TEST TEST RTE
		mw.visitLineNumber(2, new Label());
		mw.visitFieldInsn(Opcodes.PUTFIELD, testAgainst, "cause",
				"Ljava/lang/Throwable;");
		// PS TEST <>
		mw.visitLineNumber(3, new Label());
		mw.visitFieldInsn(Opcodes.GETFIELD, testAgainst, "cause",
				"Ljava/lang/Throwable;");
		// PS CAUSE
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// <>
		mw.visitInsn(Opcodes.RETURN);
		mw.visitMaxs(0, 0);

		mw.visitEnd();
	}
}
