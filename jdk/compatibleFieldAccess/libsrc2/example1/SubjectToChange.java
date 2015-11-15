package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	
	/**
	 * Now the field is private. But we have getter and setter to 
	 * access the value
	 */
	private int publicField;
	
	/**
	 * Now the static field is private. But we have getter and setter
	 * to access the value
	 */
	private static int publicStaticField = 42;
	
	public SubjectToChange(int value) {
		this.publicField = value;
	}
	
	/**
	 * static getter-accessor-Method for the property publicStaticField.
	 * type of the getter is int. name of the static property is publicStaticField.
	 * the name of the method is irrelevant.
	 */
	@Accessor("publicStaticField")
	public static int getStaticField() {
		return publicStaticField;
	}
	
	/**
	 * static setter-accessor-Method for the property publicStaticField.
	 * type of the Setter is int. name of the static property is publicStaticField.
	 * the name of the method is irrelevant.
	 */
	@Accessor("publicStaticField")
	public static void setStaticField(int newValue) {
		publicStaticField = newValue;
	}

	/**
	 * getter-accessor-Method for the property publicField.
	 * type of the getter is int. name of the property is publicField 
	 * which is direved from the methodname. 
	 */
	@Accessor()
	public int getPublicField() {
		return publicField;
	}
	
	/**
	 * setter-accessor-Method for the property publicField.
	 * type of the setter is int. name of the property is publicField 
	 * wich is direved from the methodname. 
	 */
	@Accessor()
	public void setPublicField(int value) {
		this.publicField = value;
	}
	
}
