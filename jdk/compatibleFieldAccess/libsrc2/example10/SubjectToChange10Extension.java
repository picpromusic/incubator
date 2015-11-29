package example10;

import java.lang.reflect.Accessor;

public class SubjectToChange10Extension extends SubjectToChange10 {

	private int field;

	@Accessor
	public int getField() {
		return field;
	}

	@Accessor
	public void setField(int value) {
		field = value;
	}
}
