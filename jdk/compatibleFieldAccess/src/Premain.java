import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * Class that initializes the instrumentation process.
 * 
 * @author Sebastian
 */
public class Premain {

	/**
	 * This method is called by the vm.
	 * 
	 * @param agentArgs
	 *            The agent Arguments that are definied at command line
	 * @param inst
	 *            ServiceObject for initializing Instrumentation in the VM
	 * @throws FileNotFoundException
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		String temp = System.getProperty("BCT_OUTFILE");
		String solution = System.getProperty("SolutionList");
		boolean sol1 = solution != null ? solution.contains("1") : false; // Field
																			// resolution
																			// ambiguous
		boolean sol2 = solution != null ? solution.contains("2") : false; // Field
																			// resolution
																			// unambiguous
		boolean solBoot = solution != null ? solution.contains("B") : false; // Field
																				// resolution
																				// Bootstrap

		if (sol1 && sol2) {
			throw new IllegalStateException(
					"Solution 1 and 2 cannot be used together");
		}

		/*
		 * Opens a PrintStream to the OutputFile or get System.out as Output
		 * destination
		 */
		PrintStream pw;
		if (temp != null) {
			try {
				pw = new PrintStream(temp);
			} catch (FileNotFoundException e) {
				pw = System.out;
			}
		} else {
			pw = System.out;
		}

		pw.println("ByteCodeTransformation 1=" + sol1 + " 2=" + sol2
				+ " Bootstrap=" + solBoot);

		// Eveluate the Tracelevel. The Default-Tracelevel is 0
		int traceLevel = 0;
		temp = System.getProperty("BCT_TRACE_LEVEL");
		if (temp != null) {
			pw.println("Tracelevel:" + temp);
			traceLevel = Integer.parseInt(temp);
		}

		/*
		 * Initialize and add the ClassFileTransformer to the
		 * Instrumentation-Service
		 */
		MyClassFileTransformer classFileTransformer = new MyClassFileTransformer();
		classFileTransformer.setSolution1(sol1);
		classFileTransformer.setSolution2(sol2);
		classFileTransformer.setSolutionBootstrap(solBoot);
		classFileTransformer.setTraceOutput(pw);
		classFileTransformer.setTraceLevel(traceLevel);
		inst.addTransformer(classFileTransformer);

		// Register a ShutdownHook to close the optional OutputFile
		final PrintStream fpw = pw;
		if (pw != System.out) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					fpw.close();
				}
			});
		}
	}
}
