package example5;

import java.lang.reflect.Accessor;

public class SubjectToChange {
	private int wasStaticField;
	private static int wasNonStaticField;
	
	public SubjectToChange(int value) {
		this.wasStaticField = value;
	}
	
	@Accessor("publicField")
	public static int getValue() {
		return wasNonStaticField;
	}
	
	@Accessor("publicField")
	public static void setValue(int value) {
		wasNonStaticField = value;
	}
	
	@Accessor("publicStaticField")
	public int getStatic() {
		return wasStaticField;
	}
	
	@Accessor("publicStaticField")
	public void setStatic(int newValue) {
		wasStaticField = newValue;
	}
}
