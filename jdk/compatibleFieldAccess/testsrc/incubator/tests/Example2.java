package incubator.tests;

import javalang.ref.AsymeticAcessorError;
import example2.SubjectToChange;

public class Example2 {
	public static void main(String[] args) {
		boolean expectError = Boolean.getBoolean("cfa.changedVersion");
		try {
			testIt();
			if (expectError) {
				throw new AssertionError("AsymeticAcessorError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (! (e.getCause() instanceof AsymeticAcessorError)) {
				throw new AssertionError("Error not expected", e);
			}
		} catch (AsymeticAcessorError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			}
		}
	}

	private static void testIt() {
		SubjectToChange stc = new SubjectToChange(5);
		System.out.println(stc.publicField);
	}
}
