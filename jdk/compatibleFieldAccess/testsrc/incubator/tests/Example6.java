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
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;

		testItExtension(sol2, changedVersion);
		testItFriend(sol2, changedVersion);
		testItExtensionFriend(sol2, changedVersion);
	}

	private static void testItExtensionFriend(boolean sol2,
			boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItExtensionFriend");
		SubjectToChangeExtension extend = new SubjectToChangeGoodFriend(51966);
		SubjectToChange stc = extend;

		System.out.println(stc.publicToProtectedField);
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",changedVersion);

		System.out.println(stc.publicToPackageField);
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",changedVersion);

		System.out.println(extend.getProtectedValueAsHexString());
		MethodTracer
				.check("SubjectToChangeGoodFriend.getProtectedValueAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getProtectedField()",changedVersion);

		System.out.println(extend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeGoodFriend.getPublicToPackageFieldAsHexString()");
		// No call to SubjectToChange.getPublicToPackageField() because
		// package access is allowed for extending class in same pacakge
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",sol2);

		System.out.println(extend.getPublicToProtectedFieldAsHexString());
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
		MethodTracer.checkCallCountAndClean("");
	}

	private static void testItExtension(boolean sol2, boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItExtension");

		SubjectToChangeExtension extend = new SubjectToChangeExtension(51966);
		SubjectToChange stc = extend;
		System.out.println(stc.publicToProtectedField);
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion);

		System.out.println(stc.publicToPackageField);
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
				changedVersion);

		System.out.println(extend.getProtectedValueAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getProtectedValueAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getProtectedField()",
				changedVersion);

		System.out.println(extend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToPackageFieldAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
				changedVersion);

		System.out.println(extend.getPublicToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToProtectedFieldAsHexString()");
		// No call to SubjectToChange.getPublicToProtectedField() because
		// package access is allowed for extending class
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion && sol2);

		MethodTracer.checkCallCountAndClean("");
	}

	private static void testItFriend(boolean sol2, boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItFriend");
		SubjectToChange stc = new SubjectToChange(47806);
		SubjectToChangeFriend friend = new SubjectToChangeFriend(stc);
		System.out.println(friend.getPackageValueAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPackageValueAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getPackageField()",
				changedVersion);

		System.out.println(friend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToPackageFieldAsHexString()");
		// No call to SubjectToChange.getPublicToPackageField() because
		// package access is allowed for frieds
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()", sol2);

		System.out.println(friend.getPublicToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToProtectedFieldAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion);
		MethodTracer.checkCallCountAndClean("");
	}

}
