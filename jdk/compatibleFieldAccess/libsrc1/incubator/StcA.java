package incubator;

import java.lang.reflect.Accessor;

public class StcA {

	private int field;

	@Accessor("field")
	protected void setField(int value) {
		System.out.println("setField Method of class StcA accessed");
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
