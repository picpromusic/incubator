package incubator;

import javalang.ref.Accessor;

public class StcA {

	private int field;

	@Accessor("field")
	protected void setField(int value) {
		this.field = value;
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
