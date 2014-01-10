package example6;

public class SubjectToChange {

	public int publicToProtectedField;
	public int publicToPackageField;
	protected int protectedField;
	/* package */int packageField;

	public SubjectToChange(int value) {
		this.protectedField = value;
		this.packageField = value;
		this.publicToProtectedField = value;
		this.publicToPackageField = value;
	}
	
}
