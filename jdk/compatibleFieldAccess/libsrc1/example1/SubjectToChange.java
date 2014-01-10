package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int publicField;
	private static int publicStaticField = 42;
	
	public SubjectToChange(int value) {
		this.publicField = value;
	}
	
	@Accessor("publicField")
	public int getPublicField() {
		return publicField;
	}
	
	@Accessor("publicField")
	public void setPublicField(int value) {
		this.publicField = value;
	}
	
	@Accessor("publicStaticField")
	public static int getPublicStaticField() {
		return publicStaticField;
	}
	
	@Accessor("publicStaticField")
	public static void setPublicStaticField(int newValue) {
		publicStaticField = newValue;
	}
}
