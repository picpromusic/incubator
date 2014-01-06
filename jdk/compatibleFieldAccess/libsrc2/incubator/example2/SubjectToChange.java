package example2;

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
	
}
