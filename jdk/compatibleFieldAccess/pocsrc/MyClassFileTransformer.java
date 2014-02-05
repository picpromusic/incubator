import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.CodeSigner;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

public class MyClassFileTransformer implements ClassFileTransformer {

	private static final int TRACE_ALL_CLASSNAME = 5;
	private static final int TRACE_CLASSNAME = 4;
	private static final int TRACE_SIGNED_MESSAGE = 1;
	private int traceLevel;
	private PrintStream tracer;

	public MyClassFileTransformer(Instrumentation inst) {
		traceLevel = 0;
		tracer = System.out;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		if (traceLevel > TRACE_ALL_CLASSNAME) {
			tracer.println(className);
		}
		try {
			if (protectionDomain != null) {
				CodeSigner[] codeSigners = protectionDomain.getCodeSource()
						.getCodeSigners();
				if (codeSigners != null && codeSigners.length == 0) {
					if (traceLevel > TRACE_SIGNED_MESSAGE) {
						tracer.println("This code ("+className+")is signed");
					}
					return classfileBuffer;
				}
			}
			return transform(classfileBuffer, className, loader);

		} catch (Exception e) {
			System.err.println(className);
			e.printStackTrace(System.err);
			System.exit(0);
			return null;
		}
	}

	private byte[] transform(byte[] classfileBuffer, String className,
			ClassLoader loader) throws IOException {
		ClassReader cr = new ClassReader(classfileBuffer);
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, ClassReader.EXPAND_FRAMES);
		CheckFieldAccess inserter = new CheckFieldAccess(classNode, loader);
		inserter.setTraceOutput(tracer);
		inserter.setTraceLevel(traceLevel);

		boolean transformed = false;
		boolean isExtensionOrFriend = className.endsWith("Friend")
				|| className.endsWith("Extension");
		if (className.startsWith("incubator/dependency")
				|| className.startsWith("incubator/tests/")
				|| (className.startsWith("example") && isExtensionOrFriend)) {
			if (traceLevel > TRACE_CLASSNAME) {
				tracer.println("Transform " + className);
			}
			inserter.makeItSo();
			transformed = true;
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
		if (transformed) {
			File file = new File("tmp/" + className + ".class");
			file.getParentFile().mkdirs();
			FileOutputStream fout = new FileOutputStream(file);
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

	public void setTraceOutput(PrintStream pw) {
		this.tracer = pw;
	}

	public void setTraceLevel(int traceLevel) {
		this.traceLevel = traceLevel;

	}

}