package incubator;

import javalang.ref.Accessor;

public class StcA {

	private int field;
	private int setCounter = 1;

	@Accessor("field")
	protected void setField(int value) {
		this.field = value;
		if (setCounter-- == 0) {
			throw new IllegalStateException("Not allowed to change the field more than once. This message is intendend");
		}
	}

	@Accessor("field")
	protected int getField() {
		return this.field;
	}
	
	public StcA() {
		this.field = 5;
	}

	@Override
	public String toString() {
		return "StcA:" + field;
	}

}
