package example6;

import incubator.MethodTracer;
import javalang.ref.Accessor;

public class SubjectToChange {

	protected int publicToProtectedField;
	/* package */int publicToPackageField;
	private int protectedField;
	private int packageField;

	public SubjectToChange(int value) {
		this.protectedField = value;
		this.packageField = value;
		this.publicToProtectedField = value;
		this.publicToPackageField = value;
	}

	@Accessor("publicToProtectedField")
	public int getPublicToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPublicToProtectedField()");
		return publicToProtectedField;
	}

	@Accessor("publicToProtectedField")
	public void setPublicToProtectedField(int publicToProtectedField) {
		MethodTracer.trace("SubjectToChange.setPublicToProtectedField()");
		this.publicToProtectedField = publicToProtectedField;
	}

	@Accessor("publicToPackageField")
	public int getPublicToPackageField() {
		MethodTracer.trace("SubjectToChange.getPublicToPackageField()");
		return publicToPackageField;
	}

	@Accessor("publicToPackageField")
	public void setPublicToPackageField(int publicToPackageField) {
		MethodTracer.trace("SubjectToChange.setPublicToPackageField()");
		this.publicToPackageField = publicToPackageField;
	}

	@Accessor("protectedField")
	public int getProtectedField() {
		MethodTracer.trace("SubjectToChange.getProtectedField()");
		return protectedField;
	}

	@Accessor("protectedField")
	public void setProtectedField(int protectedField) {
		MethodTracer.trace("SubjectToChange.setProtectedField()");
		this.protectedField = protectedField;
	}

	@Accessor("packageField")
	public int getPackageField() {
		MethodTracer.trace("SubjectToChange.getPackageField()");
		return packageField;
	}

	@Accessor("packageField")
	public void setPackageField(int packageField) {
		MethodTracer.trace("SubjectToChange.setPackageField()");
		this.packageField = packageField;
	}

}