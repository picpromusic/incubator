package incubator.tests;

import example10.SubjectToChange10;
import example10.SubjectToChange10Extension;
import example9.SubjectToChange9;
import example9.SubjectToChange9Extension;


public class Example10 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		SubjectToChange10Extension extend = new SubjectToChange10Extension();
		SubjectToChange10 stc = extend;
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
