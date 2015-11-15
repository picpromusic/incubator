package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	
	/**
	 * An instance variable. Use to store the value
	 * of publicField in the previous version of
	 * example1.SubjectToChange. In Solution 1 it
	 * is not allowed to have the property multiple
	 * times. So we changed it from publicField to 
	 * value
	 */
	private int value;

	/**
	 * An static variable. Use to store the value
	 * of publicStaticField in the previous version
	 * of example1.SubjectToChange. In Solution 2 it
	 * is not allowed to have the property multiple 
	 * times. So we changed it from publicStaticField 
	 * to privateStaticField.
	 */
	private static int staticValue;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	@Accessor("publicStaticField")
	public static int getStatic() {
		return staticValue;
	}
	
	@Accessor("publicStaticField")
	public static void setStatic(int newValue) {
		staticValue = newValue;
	}

	@Accessor()
	public int getPublicField() {
		return value;
	}
	
	@Accessor()
	public void setPublicField(int value) {
		this.value = value;
	}

}