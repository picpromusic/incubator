package example6.sub;

import incubator.MethodTracer;
import example6.SubjectToChange;

public class SubjectToChangeExtension extends SubjectToChange {

	public SubjectToChangeExtension(int value) {
		super(value);
	}

	public String getProtectedValueAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getProtectedValueAsHexString()");
		return Integer.toHexString(super.protectedField);
	}

	public String getPublicToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getPublicToPackageFieldAsHexString()");
		return Integer.toHexString(super.publicToPackageField);
	}

	public String getPublicToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getPublicToProtectedFieldAsHexString()");
		return Integer.toHexString(super.publicToProtectedField);
	}

}
