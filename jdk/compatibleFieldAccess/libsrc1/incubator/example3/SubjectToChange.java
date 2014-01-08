package example3;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int value;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	@Accessor("value")
	public int getValue() {
		return value;
	}

	@Accessor("value")
	public void setValue(int value) {
		this.value = value;
	}

}
