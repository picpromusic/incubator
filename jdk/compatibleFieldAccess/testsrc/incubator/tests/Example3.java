package incubator.tests;

import javalang.ref.UnambiguousFieldError;
import example4.SubjectToChange;

public class Example3 {
	public static void main(String[] args) {
		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;

		boolean expectError = Boolean.getBoolean("cfa.changedVersion") & sol2;
		
		try {
			testIt();
			if (expectError) {
				throw new AssertionError("UnambiguousFieldError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (! (e.getCause() instanceof UnambiguousFieldError)) {
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
		System.out.println(stc.value);
	}
}
