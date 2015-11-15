package example9;

import javalang.ref.Accessor;

public class SubjectToChange9 {

	private int innerField;

	@Accessor
	public int getField() {
		return innerField;
	}

	@Accessor
	public void setField(int value) {
		innerField = value;
	}

}
