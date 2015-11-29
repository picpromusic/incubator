package example10;

import java.lang.reflect.Accessor;

public class SubjectToChange10 {

	private int myField;

	@Accessor
	public int getField() {
		return myField;
	}

	@Accessor
	public void setField(int value) {
		myField = value;
	}

}
