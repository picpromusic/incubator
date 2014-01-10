package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int value;
	private static int privateStaticField;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	@Accessor("publicField")
	public int getValue() {
		return value;
	}
	
	@Accessor("publicField")
	public void setValue(int value) {
		this.value = value;
	}
	
	@Accessor("publicStaticField")
	public static int getStatic() {
		return privateStaticField;
	}
	
	@Accessor("publicStaticField")
	public static void setStatic(int newValue) {
		privateStaticField = newValue;
	}

}
