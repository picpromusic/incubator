package example6;

import incubator.MethodTracer;
import javalang.ref.Accessor;

public class SubjectToChange {

	// Changing the field to protected does not help for binary compatiblity
	// Because we have to change the name because of the ambigous field name
	// dependency
	protected int innerPublicToProtectedField;

	// Changing the field to package does not help for binary compatiblity
	// Because we have to change the name because of the ambigous field name
	// dependency
	/* package */int innerPublicToPackageField;

	private int innerProtectedField;
	private int innerPackageField;

	public SubjectToChange(int value) {
		this.innerProtectedField = value;
		this.innerPackageField = value;
		this.innerPublicToProtectedField = value;
		this.innerPublicToPackageField = value;
	}

	@Accessor("publicToProtectedField")
	public int getPublicToProtectedField() {
		MethodTracer.trace("SubjectToChange.getPublicToProtectedField()");
		return this.innerPublicToProtectedField;
	}

	@Accessor("publicToProtectedField")
	public void setPublicToProtectedField(int publicToProtectedField) {
		MethodTracer.trace("SubjectToChange.setPublicToProtectedField()");
		this.innerPublicToProtectedField = publicToProtectedField;
	}

	@Accessor("publicToPackageField")
	public int getPublicToPackageField() {
		MethodTracer.trace("SubjectToChange.getPublicToPackageField()");
		return this.innerPublicToPackageField;
	}

	@Accessor("publicToPackageField")
	public void setPublicToPackageField(int publicToPackageField) {
		MethodTracer.trace("SubjectToChange.setPublicToPackageField()");
		this.innerPublicToPackageField = publicToPackageField;
	}

	@Accessor("protectedField")
	public int getProtectedField() {
		MethodTracer.trace("SubjectToChange.getProtectedField()");
		return this.innerProtectedField;
	}

	@Accessor("protectedField")
	public void setProtectedField(int protectedField) {
		MethodTracer.trace("SubjectToChange.setProtectedField()");
		this.innerProtectedField = protectedField;
	}

	@Accessor("packageField")
	public int getPackageField() {
		MethodTracer.trace("SubjectToChange.getPackageField()");
		return this.innerPackageField;
	}

	@Accessor("packageField")
	public void setPackageField(int packageField) {
		MethodTracer.trace("SubjectToChange.setPackageField()");
		this.innerPackageField = packageField;
	}

}
