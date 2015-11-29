package example9;

import java.lang.reflect.Accessor;

public class SubjectToChange9 {

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
