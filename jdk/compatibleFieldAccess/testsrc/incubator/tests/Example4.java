package incubator.tests;

import javalang.ref.UnambiguousFieldError;
import example3.SubjectToChange;

/**
 * Example 4 shows that in solution 1 the fieldname of the changed field
 * must be changed. If it is not changed (as in this example) a
 * UnambiguousFieldError is thrown while writing to the field. 
 * 
 * @author sebastian
 *
 */
public class Example4 {
	public static void main(String[] args) {
		String solution = System.getProperty("SolutionList");
		boolean sol1 = solution != null ? solution.contains("1") : false;
		boolean sol2 = solution != null ? solution.contains("2") : false;

		boolean expectError = Boolean.getBoolean("cfa.changedVersion") & sol1;

		try {
			testIt();
			if (expectError) {
				throw new AssertionError("UnambiguousFieldError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (!(e.getCause() instanceof UnambiguousFieldError)) {
				throw new AssertionError("Error not expected", e);
			}
		} catch (UnambiguousFieldError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			}
		}
	}

	private static void testIt() {
		SubjectToChange stc = new SubjectToChange(7);
		stc.value = 8;
	}
}