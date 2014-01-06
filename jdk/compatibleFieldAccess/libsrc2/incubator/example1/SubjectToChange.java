package example1;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int value;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	@Accessor("publicField")
	public int getValue() {
		return value;
	}
	
	@Accessor("publicField")
	public void setValue(int value) {
		this.value = value;
	}
}
