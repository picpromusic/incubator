package incubator.tests;

import javalang.ref.AsymeticAcessorError;
import javalang.ref.UnambiguousFieldError;
import example3.SubjectToChange;

public class Example4 {
	public static void main(String[] args) {
		boolean expectError = Boolean.getBoolean("cfa.changedVersion");
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
		stc.value = 8;
	}
}
