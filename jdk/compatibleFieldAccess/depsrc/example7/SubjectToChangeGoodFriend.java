package example7;

import incubator.MethodTracer;
import example7.sub.SubjectToChangeExtension;

public class SubjectToChangeGoodFriend extends SubjectToChangeExtension {

	public SubjectToChangeGoodFriend(int value) {
		super(value);
	}

	public String getProtectedToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getProtectedToPackageFieldAsHexString()");
		return Integer.toHexString(protectedToPackageField);
	}

	public String getPackageToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getPackageToProtectedFieldAsHexString()");
		return Integer.toHexString(((SubjectToChange)this).packageToProtectedField);
	}

	public String getPublicToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getPublicToPackageFieldAsHexString()");
		return Integer.toHexString(publicToPackageField);
	}

	public String getPublicToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getPublicToProtectedFieldAsHexString()");
		return Integer.toHexString(publicToProtectedField);
	}

}
