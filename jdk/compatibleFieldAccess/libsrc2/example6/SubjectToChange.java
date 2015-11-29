package example6;

import incubator.MethodTracer;
import java.lang.reflect.Accessor;

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
	protected int getProtectedFieldWithProtectedAccessor() {
		MethodTracer.trace("SubjectToChange.getProtectedFieldWithProtectedAccessor()");
		return protectedField;
	}

	@Accessor("protectedField")
	protected void setProtectedFieldWithProtectedAccessor(int protectedField) {
		MethodTracer.trace("SubjectToChange.setProtectedFieldWithProtectedAccessor()");
		this.protectedField = protectedField;
	}

	@Accessor("packageField")
	int getPackageFieldWithPackageAccessor() {
		MethodTracer.trace("SubjectToChange.getPackageFieldWithPackageAccessor()");
		return packageField;
	}

	@Accessor("packageField")
	void setPackageFieldWithPackageAccessor(int packageField) {
		MethodTracer.trace("SubjectToChange.setPackageFieldWithPackageAccessor()");
		this.packageField = packageField;
	}

}