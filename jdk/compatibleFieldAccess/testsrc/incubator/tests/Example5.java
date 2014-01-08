package incubator.tests;

import example5.SubjectToChange;

public class Example5 {
	public static void main(String[] args) {

		boolean expectError = Boolean.getBoolean("cfa.changedVersion");
		
		try {
			testItStatic();
			if (expectError) {
				throw new AssertionError("IncompatibleClassChangeError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (! (e.getCause() instanceof IncompatibleClassChangeError)) {
				throw new AssertionError("Error not expected", e);
			}
		} catch (IncompatibleClassChangeError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			}
		}

		try {
			testIt();
			if (expectError) {
				throw new AssertionError("IncompatibleClassChangeError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (! (e.getCause() instanceof IncompatibleClassChangeError)) {
				throw new AssertionError("Error not expected", e);
			}
		} catch (IncompatibleClassChangeError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			}
		}
	}

	private static void testIt() {
		SubjectToChange stc = new SubjectToChange(7);
		System.out.println(stc.publicField);
	}

	private static void testItStatic() {
		System.out.println(SubjectToChange.publicStaticField);
	}
}
