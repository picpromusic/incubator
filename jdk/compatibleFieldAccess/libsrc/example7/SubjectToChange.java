package example7;

public class SubjectToChange {

	public int publicToProtectedField;
	public int publicToPackageField;
	protected int protectedToPackageField;
	int packageToProtectedField;

	public SubjectToChange(int value) {
		this.protectedToPackageField = value;
		this.packageToProtectedField = value;
		this.publicToProtectedField = value;
		this.publicToPackageField = value;
	}

}
