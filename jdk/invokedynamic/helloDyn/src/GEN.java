import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GEN {
	public static void main(String[] args) throws IOException {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES
				| ClassWriter.COMPUTE_MAXS);
		// cw.newClass("G");
		cw.visit(51, Opcodes.ACC_PUBLIC, "test/G", null, "java/lang/Object", null);

		
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC
				| Opcodes.ACC_STATIC, "call", "()V", null, null);
		mw.visitCode();
//		mw.visitMethodInsn(Opcodes.INVOKESTATIC, "M", "cafeBabe", "()V");
		MethodHandle BSM_LEFT = new MethodHandle(
				Opcodes.MH_INVOKESTATIC,
				"test/M",
				"bootstrap",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;I)Ljava/lang/invoke/CallSite;");
		mw.visitInvokeDynamicInsn("dyn", "()V", BSM_LEFT, 1);
		mw.visitInsn(Opcodes.RETURN);
		mw.visitMaxs(0, 0);

		mw.visitEnd();
		cw.visitEnd();

		byte[] byteArray = cw.toByteArray();
		System.out.println(byteArray.length);
		FileOutputStream fout = new FileOutputStream("gen/test/G.class");
		fout.write(byteArray);
		fout.close();
		
	}
}
