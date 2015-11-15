package example7;

import incubator.MethodTracer;
import javalang.ref.Accessor;

public class SubjectToChange {

	protected int publicToProtectedField;
	/* package */int publicToPackageField;
	/* package */int protectedToPackageField;
	protected int packageToProtectedField;

	public SubjectToChange(int value) {
		this.publicToProtectedField = value;
		this.publicToPackageField = value;
		this.protectedToPackageField = value;
		this.packageToProtectedField = value;
	}

	@Accessor()
	public int getPublicToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPublicToProtectedField()");
		return publicToProtectedField;
	}

	@Accessor
	public void setPublicToProtectedField(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToProtectedField()");
		this.publicToProtectedField = value;
	}

	@Accessor("publicToProtectedField")
	/*package*/ int getPublicToProtectedFieldPackageLevelAccess() {
		MethodTracer.trace("SubjectToChange.getPublicToProtectedFieldPackageLevelAccess()");
		return publicToProtectedField;
	}

	@Accessor("publicToProtectedField")
	/*package*/ void setPublicToProtectedFieldPackageLevelAccess(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToProtectedFieldPackageLevelAccess()");
		this.publicToProtectedField = value;
	}
	
	@Accessor
	public int getPublicToPackageField() {
		MethodTracer.trace("SubjectToChange.getPublicToPackageField()");
		return publicToPackageField;
	}

	@Accessor
	public void setPublicToPackageField(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToPackageField()");
		this.publicToPackageField = value;
	}

	@Accessor("publicToPackageField")
	protected int getPublicToPackageFieldProtectedLevelAccess() {
		MethodTracer.trace("SubjectToChange.getPublicToPackageFieldProtectedLevelAccess()");
		return publicToPackageField;
	}

	@Accessor("publicToPackageField")
	protected void setPublicToPackageFieldProtectedLevelAccess(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToPackageFieldProtectedLevelAccess()");
		this.publicToPackageField = value;
	}

	@Accessor
	protected int getProtectedToPackageField() {
		MethodTracer.trace("SubjectToChange.getProtectedToPackageField()");
		return protectedToPackageField;
	}

	@Accessor
	protected void setProtectedToPackageField(int value) {
		MethodTracer.trace("SubjectToChange.setProtectedToPackageField()");
		this.protectedToPackageField = value;
	}

	@Accessor
	/* package */int getPackageToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPackageToProtectedField()");
		return packageToProtectedField;
	}

	@Accessor
	/* package */void setPackageToProtectedField(int value) {
		MethodTracer.trace("SubjectToChange.setPackageToProtectedField()");
		this.packageToProtectedField = value;
	}

}
