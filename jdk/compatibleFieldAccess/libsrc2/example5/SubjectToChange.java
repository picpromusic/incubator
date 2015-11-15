package example5;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int publicField;
	private static int publicStaticField = 42;
	
	public SubjectToChange(int value) {
		this.publicField = value;
	}
	
	@Accessor("publicField")
	public static int getPublicField() {
		return publicStaticField;
	}
	
	@Accessor("publicField")
	public static void setPublicField(int value) {
		publicStaticField = value;
	}
	
	@Accessor("publicStaticField")
	public int getPublicStaticField() {
		return publicField;
	}
	
	@Accessor("publicStaticField")
	public void setPublicStaticField(int newValue) {
		publicField = newValue;
	}
}
