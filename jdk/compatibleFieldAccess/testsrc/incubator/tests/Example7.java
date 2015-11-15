package incubator.tests;

import incubator.MethodTracer;
import example7.SubjectToChange;
import example7.SubjectToChangeFriend;
import example7.SubjectToChangeGoodFriend;
import example7.sub.SubjectToChangeExtension;

public class Example7 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		testItExtension(sol1, sol2, changedVersion);
		testItFriend(sol1, sol2, changedVersion);
		testItExtensionFriend(sol1, sol2, changedVersion);
	}

	private static void testItExtensionFriend(boolean sol1, boolean sol2,
			boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItExtensionFriend");
		SubjectToChangeExtension extend = new SubjectToChangeGoodFriend(51966);
		SubjectToChange stc = extend;

		System.out.println(stc.publicToProtectedField);
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion);

		System.out.println(stc.publicToPackageField);
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
				changedVersion);

		System.out.println(extend.getProtectedToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeGoodFriend.getProtectedToPackageFieldAsHexString()");
		// No call to SubjectToChange.getProtectedToPackageField() because
		// package access is allowed for extending class in same package
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getProtectedToPackageField()",
				changedVersion && sol1);

		System.out.println(extend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeGoodFriend.getPublicToPackageFieldAsHexString()");
		// No call to SubjectToChange.getPublicToPackageField() because
		// package access is allowed for extending class in same package
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
				changedVersion && sol1);

		System.out.println(extend.getPublicToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeGoodFriend.getPublicToProtectedFieldAsHexString()");
		if (!sol1) {
			// No call to SubjectToChange.getPublicToProtectedField() because
			// package access is allowed for extending class in same package
		} else {
			// The above fact feels quite naturally, but in solution 1. Where it
			// is not possible to have fields with the same name as the
			// accessor-Methods. The calls goes through the public method
			MethodTracer.check("SubjectToChange.getPublicToProtectedField()");
		}
		MethodTracer.checkCallCountAndClean("");
	}

	private static void testItExtension(boolean sol1, boolean sol2,
			boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItExtension");

		SubjectToChangeExtension extend = new SubjectToChangeExtension(51966);
		SubjectToChange stc = extend;
		System.out.println(stc.publicToProtectedField);
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion);

		System.out.println(stc.publicToPackageField);
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
				changedVersion);

		System.out.println(extend.getProtectedToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getProtectedToPackageFieldAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getProtectedToPackageField()",
				changedVersion);

		System.out.println(extend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToPackageFieldAsHexString()");
		if (changedVersion) {
			MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()",
					sol1);
			MethodTracer
					.checkIf(
							"SubjectToChange.getPublicToPackageFieldProtectedLevelAccess()",
							sol2);
		}

		System.out.println(extend.getPublicToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeExtension.getPublicToProtectedFieldAsHexString()");
		// No call to SubjectToChange.getPublicToProtectedField() because
		// protected access is allowed for extending class
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToProtectedField()",
				changedVersion && sol1);

		MethodTracer.checkCallCountAndClean("");
	}

	private static void testItFriend(boolean sol1,boolean sol2, boolean changedVersion) {
		MethodTracer.checkCallCountAndClean("testItFriend");
		SubjectToChange stc = new SubjectToChange(47806);
		SubjectToChangeFriend friend = new SubjectToChangeFriend(stc);
		System.out.println(friend.getPackageToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPackageToProtectedFieldAsHexString()");
		MethodTracer.checkIf("SubjectToChange.getPackageToProtectedField()",
				changedVersion);

		System.out.println(friend.getPublicToPackageFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToPackageFieldAsHexString()");
		// No call to SubjectToChange.getPublicToPackageField() because
		// package access is allowed for friends
		// The above fact feels quite naturally, but in solution 2. Where it
		// is not possible to have fields with the same name as the
		// accessor-Methods. The calls goes through the public method
		MethodTracer.checkIf("SubjectToChange.getPublicToPackageField()", sol1);

		System.out.println(friend.getPublicToProtectedFieldAsHexString());
		MethodTracer
				.check("SubjectToChangeFriend.getPublicToProtectedFieldAsHexString()");
		if (changedVersion) {
			MethodTracer
			.checkIf(
					"SubjectToChange.getPublicToProtectedFieldPackageLevelAccess()",
					sol2);
			MethodTracer
			.checkIf(
					"SubjectToChange.getPublicToProtectedField()",
					sol1);
		}
		MethodTracer.checkCallCountAndClean("");
	}

}
