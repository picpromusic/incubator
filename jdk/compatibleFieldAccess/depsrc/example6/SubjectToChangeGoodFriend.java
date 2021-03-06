package example6;

import incubator.MethodTracer;
import example6.sub.SubjectToChangeExtension;

public class SubjectToChangeGoodFriend extends SubjectToChangeExtension {

	public SubjectToChangeGoodFriend(int value) {
		super(value);
	}

	public String getProtectedValueAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getProtectedValueAsHexString()");
		return Integer.toHexString(protectedField);
	}
	
	public String getPackageValueAsHexString() {
		MethodTracer
				.trace("SubjectToChangeGoodFriend.getPackageValueAsHexString()");
		return Integer.toHexString(((SubjectToChange)this).packageField);
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
