import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GEN {

	/**
	 * Generates the Test Classes TestOld,TestNewSolution2 and 
	 * TestNew2Solution12. 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Path genPath = Paths.get("gen");
		Files.createDirectories(genPath);
		genClass(genPath, "TestOld", "OLD");
		genClass(genPath, "TestNewSolution2", "NEWSol2");
		genClass(genPath, "TestNew2Solution12", "NEW2Sol12");
		genClass(genPath, "TestNew3Solution12", "NEW3Sol12");
	}

	/**
	 * Generate a Testclass which test some teststructure againts another class.
	 * 
	 * @param genPath
	 *            The path to generate the Classfile to.
	 * @param className
	 *            The name of the Class to generate
	 * @param testAgainst
	 *            Name of the Class that is tested by the generated class.
	 * 
	 * @throws IOException
	 *             if the Class cannot written to disc.
	 */
	private static void genClass(Path genPath, String className,
			String testAgainst) throws IOException {
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
		Files.write(genPath.resolve(className + ".class"), byteArray,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE);
	}

	/**
	 * Generates the Method testItStatically which does something like this:
	 * 
	 * <pre>
	 * System.out.println(testAgainst.staticField);
	 * testAgainst.staticField = new Object;
	 * System.out.println(testAgainst.staticField);
	 * </pre>
	 * 
	 * @param testAgainst
	 *            the Classtype which is used for the teststructure
	 * @param cw
	 *            classWriter to write the method "testItStatically" to.
	 */
	private static void genStaticTest(String testAgainst, ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC
				| Opcodes.ACC_STATIC, "testItStatically", "()V", null, null);
		mw.visitCode();
		mw.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		// PS
		mw.visitInsn(Opcodes.DUP);
		// PS PS
		mw.visitFieldInsn(Opcodes.GETSTATIC, testAgainst, "staticField",
				"Ljava/lang/Object;");
		// PS PS FIELD
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// PS
		mw.visitTypeInsn(Opcodes.NEW, "java/lang/Object");
		// PS NEW_VALUE
		mw.visitInsn(Opcodes.DUP);
		// PS NEW_VALUE NEW_VALUE
		mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
				"()V");
		// PS NEW_VALUE
		mw.visitFieldInsn(Opcodes.PUTSTATIC, testAgainst, "staticField",
				"Ljava/lang/Object;");
		// PS
		mw.visitFieldInsn(Opcodes.GETSTATIC, testAgainst, "staticField",
				"Ljava/lang/Object;");
		// PS NEW_VALUE
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/Object;)V");
		// <>
		mw.visitInsn(Opcodes.RETURN);
		mw.visitMaxs(0, 0);
		mw.visitEnd();

	}

	/**
	 * Generates the Method testIt which does something like this:
	 * 
	 * <pre>
	 * testAgainst tObject = new testAgainst();
	 * System.out.println(tObject.cause);
	 * tObject.cause = new RuntimeException(&quot;NEW&quot;);
	 * System.out.println(tObject.cause);
	 * </pre>
	 * 
	 * It works without the local variable but "ansonsten" it is basically the
	 * same.
	 * 
	 * @param testAgainst
	 *            the Classtype which is used for the teststructure
	 * @param cw
	 *            classWriter to write the method "testIt" to.
	 */
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
