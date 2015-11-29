package example4;

import java.lang.reflect.Accessor;

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
