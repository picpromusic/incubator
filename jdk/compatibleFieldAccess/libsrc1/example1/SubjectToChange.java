package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int publicField;
	private static int publicStaticField = 42;
	
	public SubjectToChange(int value) {
		this.publicField = value;
	}
	
	@Accessor()
	public int getPublicField() {
		return publicField;
	}
	
	@Accessor()
	public void setPublicField(int value) {
		this.publicField = value;
	}
	
	@Accessor("publicStaticField")
	public static int getStaticField() {
		return publicStaticField;
	}
	
	@Accessor("publicStaticField")
	public static void setStaticField(int newValue) {
		publicStaticField = newValue;
	}
}
