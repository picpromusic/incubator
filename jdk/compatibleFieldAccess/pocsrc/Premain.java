import java.io.FileNotFoundException;
import java.io.PrintStream;
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
		if (inst.isRetransformClassesSupported()) {
			MyClassFileTransformer classFileTransformer = new MyClassFileTransformer(
					inst);
			classFileTransformer.setTraceOutput(pw);
			classFileTransformer.setTraceLevel(traceLevel);
			inst.addTransformer(classFileTransformer, true);
		}

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