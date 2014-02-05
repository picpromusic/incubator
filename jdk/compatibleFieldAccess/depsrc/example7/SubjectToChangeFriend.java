package example7;

import incubator.MethodTracer;

public class SubjectToChangeFriend {

	private SubjectToChange stc;

	public SubjectToChangeFriend(SubjectToChange stc) {
		this.stc = stc;
	}

	public String getPackageToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeFriend.getPackageToProtectedFieldAsHexString()");
		return Integer.toHexString(stc.packageToProtectedField);
	}

	public String getPublicToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeFriend.getPublicToPackageFieldAsHexString()");
		return Integer.toHexString(stc.publicToPackageField);
	}

	public String getPublicToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeFriend.getPublicToProtectedFieldAsHexString()");
		return Integer.toHexString(stc.publicToProtectedField);
	}

}
