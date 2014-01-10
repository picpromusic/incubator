package incubator.tests;

import java.util.Iterator;
import java.util.List;

import incubator.MethodTracer;
import example6.SubjectToChange;
import example6.SubjectToChangeFriend;
import example6.SubjectToChangeGoodFriend;
import example6.sub.SubjectToChangeExtension;

public class Example6 {
	public static void main(String[] args) {

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;

		testItExtension(sol2);
		testItFriend(sol2);
		testItExtensionFriend(sol2);
	}

	private static void testItExtensionFriend(boolean sol2) {
		MethodTracer.checkCallCountAndClean();
		SubjectToChangeExtension extend = new SubjectToChangeGoodFriend(51966);
		SubjectToChange stc = extend;
		System.out.println(stc.publicToProtectedField);
		System.out.println(stc.publicToPackageField);
		System.out.println(extend.getProtectedValueAsHexString());
		System.out.println(extend.getPublicToPackageFieldAsHexString());
		System.out.println(extend.getPublicToProtectedFieldAsHexString());
		MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		MethodTracer.check("SubjectToChange.getPublicToPackageField()");
		MethodTracer
				.check("SubjectToChangeGoodFriend.getProtectedValueAsHexString()");
		MethodTracer.check("SubjectToChange.getProtectedField()");
		MethodTracer
				.check("SubjectToChangeGoodFriend.getPublicToPackageFieldAsHexString()");
		if (!sol2) {
			// No call to SubjectToChange.getPublicToPackageField() because
			// package access is allowed for extending class in same pacakge
		} else {
			// The above fact feels quite naturally, but in solution 2. Where it
			// is not possible to have fields with the same name as the
			// accessor-Methods. The calls goes through the public method
			MethodTracer.check("SubjectToChange.getPublicToPackageField()");
		}
		MethodTracer
				.check("SubjectToChangeGoodFriend.getPublicToProtectedFieldAsHexString()");
		if (!sol2) {
			// No call to SubjectToChange.getPublicToProtectedField() because
			// package access is allowed for extending class in same package
		} else {
			// The above fact feels quite naturally, but in solution 2. Where it
			// is not possible to have fields with the same name as the
			// accessor-Methods. The calls goes through the public method
			MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		}
		MethodTracer.checkCallCountAndClean();
	}

	private static void testItExtension(boolean sol2) {
		MethodTracer.checkCallCountAndClean();
		SubjectToChangeExtension extend = new SubjectToChangeExtension(51966);
		SubjectToChange stc = extend;
		System.out.println(stc.publicToProtectedField);
		System.out.println(stc.publicToPackageField);
		System.out.println(extend.getProtectedValueAsHexString());
		System.out.println(extend.getPublicToPackageFieldAsHexString());
		System.out.println(extend.getPublicToProtectedFieldAsHexString());
		MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		MethodTracer.check("SubjectToChange.getPublicToPackageField()");
		MethodTracer
				.check("SubjectToChangeExtension.getProtectedValueAsHexString()");
		MethodTracer.check("SubjectToChange.getProtectedField()");
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToPackageFieldAsHexString()");
		MethodTracer.check("SubjectToChange.getPublicToPackageField()");
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToProtectedFieldAsHexString()");
		if (!sol2) {
			// No call to SubjectToChange.getPublicToProtectedField() because
			// package access is allowed for extending class
		} else {
			// The above fact feels quite naturally, but in solution 2. Where it
			// is not possible to have fields with the same name as the
			// accessor-Methods. The calls goes through the public method
			MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		}
		MethodTracer.checkCallCountAndClean();
	}

	private static void testItFriend(boolean sol2) {
		MethodTracer.checkCallCountAndClean();
		SubjectToChange stc = new SubjectToChange(47806);
		SubjectToChangeFriend friend = new SubjectToChangeFriend(stc);
		System.out.println(friend.getPackageValueAsHexString());
		System.out.println(friend.getPublicToPackageFieldAsHexString());
		System.out.println(friend.getPublicToProtectedFieldAsHexString());
		int index = 0;
		MethodTracer
				.check("SubjectToChangeFriend.getPackageValueAsHexString()");
		MethodTracer.check("SubjectToChange.getPackageField()");
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToPackageFieldAsHexString()");
		if (!sol2) {
			// No call to SubjectToChange.getPublicToPackageField() because
			// package access is allowed for frieds
		} else {
			// The above fact feels quite naturally, but in solution 2. Where it
			// is not possible to have fields with the same name as the
			// accessor-Methods. The calls goes through the public method
			MethodTracer.check("SubjectToChange.getPublicToPackageField()");
		}
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToProtectedFieldAsHexString()");
		MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		MethodTracer.checkCallCountAndClean();
	}

}
