package incubator.tests;

import example8.SubjectToChange8Extension;

public class Example8 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		SubjectToChange8Extension extend = new SubjectToChange8Extension();
		SubjectToChange8 stc = extend;
		extend.pField = 1;
		stc.pField = 2;
		if (extend.pField != 1)
			throw new AssertionError("1 expected. Got:" + extend.pField);
		if (stc.pField != 2)
			throw new AssertionError("2 expected. Got:" + stc.pField);
		extend.pField = -1;
		stc.pField = -2;
		if (!changedVersion) {
			if (extend.pField != -1)
				throw new AssertionError("1 expected. Got:" + extend.pField);
		}else {
			if (extend.pField != 0)
				throw new AssertionError("0 expected. Got:" + extend.pField);
		}
		if (stc.pField != -2)
			throw new AssertionError("2 expected. Got:" + stc.pField);
	}

}
