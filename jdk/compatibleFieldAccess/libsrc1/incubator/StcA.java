package incubator;

import java.util.Arrays;

import javalang.ref.Accessor;

public class StcA {

	private int field;
	private int setCounter = 1;

	@Accessor("field")
	protected void setField(int value) {
		System.out.println("setField Method of class StcA accessed:" + Arrays.toString(Thread.currentThread().getStackTrace()));
		if (setCounter-- == 0) {
			throw new IllegalStateException("Not allowed to change the field more than once. This message is intendend");
		}
		this.field = value;
	}

	@Accessor("field")
	protected int getField() {
		System.out.println("getField Method of class StcA accessed:" + Arrays.toString(Thread.currentThread().getStackTrace()));
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
