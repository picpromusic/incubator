package incubator.tests;

import example5.SubjectToChange;

public class CopyOfExample5 {
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

	}

	private static boolean expectedInThisSolution(String solution,
			BootstrapMethodError e) {
		boolean sol1 = solution != null ? solution.contains("1") : false;
		boolean sol2 = solution != null ? solution.contains("2") : false;

		if (sol2 && !(e.getCause() instanceof IncompatibleClassChangeError)) {
			return false;
		}
		if (sol1 && e.getCause() instanceof IllegalAccessException) {
			if (!e.getCause().getMessage()
					.equals("One or more IllegalAccessException thrown")) {
				return false;
			}
		}
		return true;
	}

	private static void testItStatic() {
		System.out.println(SubjectToChange.publicStaticField);
	}
}
