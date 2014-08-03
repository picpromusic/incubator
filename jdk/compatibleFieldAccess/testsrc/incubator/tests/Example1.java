package incubator.tests;

import example1.SubjectToChange;

/**
 * In Example 1 everything is fine.
 * 
 * There is a SubjectToChange that has two int fields. 1. an public instance
 * field 2. an public static field
 * 
 * both named accordingly to their declaration
 * 
 * The Value based via the constructor is stores in the public in publicField.
 * The value of publicStaticField is initialized to 42.
 * 
 * The Example than prints out the two values by prior accessing it via the
 * GETFIELD or GETSTATIC bytescodes.
 * 
 * This is the unchanged part of the Example. But now we want to change our 
 * public fields to private one and provide access throught getter/setter pairs.
 * 
 * See libsrc1/example1/SubjectToChange.java and libsrc1/example2/SubjectToChange.java
 * for two implementations that changes the accessiblity of both fields and guard
 * the access through accesor-methods.
 * 
 * @author Sebastian
 */
public class Example1 {
	public static void main(String[] args) {
		SubjectToChange stc = new SubjectToChange(5);
		System.out.println(stc.publicField);
		System.out.println(SubjectToChange.publicStaticField);
	}
}
