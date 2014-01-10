package example6;

import incubator.MethodTracer;

public class SubjectToChangeFriend {

	private SubjectToChange stc;

	public SubjectToChangeFriend(SubjectToChange stc) {
		this.stc = stc;
	}

	public String getPackageValueAsHexString() {
		MethodTracer
				.trace("SubjectToChangeFriend.getPackageValueAsHexString()");
		return Integer.toHexString(stc.packageField);
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
