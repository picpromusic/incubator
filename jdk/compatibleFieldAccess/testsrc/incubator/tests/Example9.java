package incubator.tests;

import example9.SubjectToChange9;
import example9.SubjectToChange9Extension;


public class Example9 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		SubjectToChange9Extension extend = new SubjectToChange9Extension();
		SubjectToChange9 stc = extend;
		extend.field = 1;
		stc.field = 2;
		System.out.println(extend.field);
		System.out.println(stc.field);
//		if (extend.field != 1)
//			throw new AssertionError("1 expected. Got:" + extend.field);
//		if (stc.field != 2)
//			throw new AssertionError("2 expected. Got:" + stc.field);
		extend.field = -1;
		stc.field = -2;
		System.out.println(extend.field);
		System.out.println(stc.field);
//		if (!changedVersion) {
//			if (extend.field != -1)
//				throw new AssertionError("1 expected. Got:" + extend.field);
//		}else {
//			if (extend.field != 0)
//				throw new AssertionError("0 expected. Got:" + extend.field);
//		}
//		if (stc.field != -2)
//			throw new AssertionError("2 expected. Got:" + stc.field);
	}

}
