package example5;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int value;
	private static int privateStaticField;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	@Accessor("publicField")
	public static int getValue() {
		return privateStaticField;
	}
	
	@Accessor("publicField")
	public static void setValue(int value) {
		privateStaticField = value;
	}
	
	@Accessor("publicStaticField")
	public int getStatic() {
		return value;
	}
	
	@Accessor("publicStaticField")
	public void setStatic(int newValue) {
		value = newValue;
	}

}
