package example7;

import incubator.MethodTracer;
import javalang.ref.Accessor;

public class SubjectToChange {

	protected int innerPublicToProtectedField;
	/* package */int innerPublicToPackageField;
	/* package */int innerProtectedToPackageField;
	protected int innerPackageToProtectedField;

	public SubjectToChange(int value) {
		this.innerPublicToProtectedField = value;
		this.innerPublicToPackageField = value;
		this.innerProtectedToPackageField = value;
		this.innerPackageToProtectedField = value;
	}

	@Accessor()
	public int getPublicToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPublicToProtectedField()");
		return innerPublicToProtectedField;
	}

	@Accessor
	public void setPublicToProtectedField(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToProtectedField()");
		this.innerPublicToProtectedField = value;
	}

	@Accessor
	public int getPublicToPackageField() {
		MethodTracer.trace("SubjectToChange.getPublicToPackageField()");
		return innerPublicToPackageField;
	}

	@Accessor
	public void setPublicToPackageField(int value) {
		MethodTracer.trace("SubjectToChange.setPublicToPackageField()");
		this.innerPublicToPackageField = value;
	}

	@Accessor
	protected int getProtectedToPackageField() {
		MethodTracer.trace("SubjectToChange.getProtectedToPackageField()");
		return innerProtectedToPackageField;
	}

	@Accessor
	protected void setProtectedToPackageField(int value) {
		MethodTracer.trace("SubjectToChange.setProtectedToPackageField()");
		this.innerProtectedToPackageField = value;
	}

	@Accessor
	/* package */int getPackageToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPackageToProtectedField()");
		return innerPackageToProtectedField;
	}

	@Accessor
	/* package */void setPackageToProtectedField(int value) {
		MethodTracer.trace("SubjectToChange.setPackageToProtectedField()");
		this.innerPackageToProtectedField = value;
	}

}
