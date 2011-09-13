import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.CodeSigner;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

public class MyClassFileTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
//		System.out.println(className);
		try {
			if (protectionDomain != null) {
				CodeSigner[] codeSigners = protectionDomain.getCodeSource()
						.getCodeSigners();
				if (codeSigners != null && codeSigners.length == 0) {
					return classfileBuffer;
				}
			}
			return transform(classfileBuffer, className);

		} catch (Exception e) {
			System.out.println(className);
			e.printStackTrace(System.err);
			System.exit(0);
			return null;
		}
	}

	private byte[] transform(byte[] classfileBuffer, String className) throws IOException {
		ClassReader cr = new ClassReader(classfileBuffer);
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, ClassReader.EXPAND_FRAMES);
		CheckFieldAccess inserter = new CheckFieldAccess(classNode);
		if (className.startsWith("Test")) {
			inserter.makeItSo();
		}
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS) {
			/*
			 * protected String getCommonSuperClass(String type1, String type2)
			 * { try { return super.getCommonSuperClass(type1, type2); } catch
			 * (Exception e) { // e.printStackTrace(System.err); return
			 * "java/lang/Object"; } }
			 */
		};
		classNode.accept(cw);
		// analyse(classNode);

		byte[] byteArray = cw.toByteArray();
		if (className.startsWith("Test")) {
			FileOutputStream fout = new FileOutputStream("tmp/" + className
					+ ".class");
			fout.write(byteArray);
			fout.close();
			printAsm(byteArray, new File("tmp", className + ".txt"));
		}
		
		return byteArray;
	}
	
	private static void printAsm(byte[] transformed, File outp)
			throws FileNotFoundException {
		ClassReader classReader = new ClassReader(transformed);
		PrintWriter pw;
		pw = new PrintWriter(new FileOutputStream(outp));
		TraceClassVisitor traceClassVisitor = new TraceClassVisitor(pw);
		classReader.accept(traceClassVisitor, ClassReader.EXPAND_FRAMES);
		pw.close();

	}
	

}
