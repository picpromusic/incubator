package example7.sub;

import incubator.MethodTracer;
import example7.SubjectToChange;

public class SubjectToChangeExtension extends SubjectToChange {

	public SubjectToChangeExtension(int value) {
		super(value);
	}

	public String getProtectedToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getProtectedToPackageFieldAsHexString()");
		return Integer.toHexString(protectedToPackageField);
	}

	public String getPublicToPackageFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getPublicToPackageFieldAsHexString()");
		return Integer.toHexString(publicToPackageField);
	}

	public String getPublicToProtectedFieldAsHexString() {
		MethodTracer
				.trace("SubjectToChangeExtension.getPublicToProtectedFieldAsHexString()");
		return Integer.toHexString(publicToProtectedField);
	}

}
