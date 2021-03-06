package incubator.tests;

import example5.SubjectToChange;

/**
 * In Example 5 we test what happens when an static access is changed to be using
 * a non-static Accessor-Method and vice-versa. It is expected to throw an 
 * IncompatibleClassChangeError, which is also the case when changing a field 
 * from static to non-static and vice-versa.
 * 
 * @author sebastian
 *
 */
public class Example5 {
	public static void main(String[] args) {

		boolean expectError = Boolean.getBoolean("cfa.changedVersion");
		String solution = System.getProperty("SolutionList");

		try {
			testItStatic();
			if (expectError) {
				throw new AssertionError(
						"IncompatibleClassChangeError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (!expectedInThisSolution(solution, e)) {
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
				throw new AssertionError(
						"IncompatibleClassChangeError expected");
			}
		} catch (BootstrapMethodError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			} else if (!expectedInThisSolution(solution, e)) {
				throw new AssertionError("Error not expected", e);
			}
		} catch (IncompatibleClassChangeError e) {
			if (!expectError) {
				throw new AssertionError("Error not expected", e);
			}
		}
	}

	private static boolean expectedInThisSolution(String solution,
			BootstrapMethodError e) {
		boolean sol1 = solution != null ? solution.contains("1") : false;
		boolean sol2 = solution != null ? solution.contains("2") : false;

		if (sol1 && !(e.getCause() instanceof IncompatibleClassChangeError)) {
			return false;
		}
		if (sol2 && e.getCause() instanceof IncompatibleClassChangeError) {
			if (!e.getCause().getMessage()
					.equals("One or more IncompatibleClassChangeError thrown")) {
				return false;
			}
		}
		return true;
	}

	private static void testIt() {
		SubjectToChange stc = new SubjectToChange(7);
		System.out.println(stc.publicField);
	}

	private static void testItStatic() {
		System.out.println(SubjectToChange.publicStaticField);
	}
}
