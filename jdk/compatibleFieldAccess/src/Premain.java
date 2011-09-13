
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
		// Evalutate which feature should be disabled
		// boolean disableStatic = isDisabled("Static");
		// boolean disableConstructor = isDisabled("Constructor");
		// boolean disableThread = isDisabled("Thread");
		// boolean disabledThreadLocalConfiguration =
		// isDisabled("ThreadLocalConfiguration");
		// if (disabledThreadLocalConfiguration) {
		// if (disableThread) {
		// Internal.switchToGlobalConfigurationObjects();
		// } else {
		// System.err
		// .println("It is only valid to disable the ThreadLocalConfiguration if -DMIdisableThread is set");
		// System.exit(0);
		// }
		// }
		// boolean disableTestAnnotation = isDisabled("TestAnnotation");

		// Evalutate the optional OutputFile
		String temp = System.getProperty("MI_OUTFILE");

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

		pw.println("MOCKINJECT BCI");

		// Eveluate the Tracelevel. The Default-Tracelevel is 0
		int traceLevel = 0;
		temp = System.getProperty("MI_TRACE_LEVEL");
		if (temp != null) {
			pw.println("Tracelevel:" + temp);
			traceLevel = Integer.parseInt(temp);
		}

		/*
		 * Initialize and add the ClassFileTransformer to the
		 * Instrumentation-Service
		 */
		ClassFileTransformer classFileTransformer = new MyClassFileTransformer();
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

	private static boolean isEnabled(String feature) {
		String disableString = System.getProperty("MIenable" + feature);
		return disableString != null;
	}

	/**
	 * Chekcs if the features is disabled. A feature is disabled if there is a
	 * System Property with the Name "MIdisbale[featurename]"
	 * 
	 * @param feature
	 *            the name of the Feature
	 * @return true if the feature should be diasbled
	 */
	private static boolean isDisabled(String feature) {
		String disableString = System.getProperty("MIdisable" + feature);
		return disableString != null;
	}
}
