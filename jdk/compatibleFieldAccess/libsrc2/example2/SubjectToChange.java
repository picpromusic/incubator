package example2;

import javalang.ref.Accessor;

public class SubjectToChange {
	private int value;
	
	public SubjectToChange(int value) {
		this.value = value;
	}
	
	/**
	 * Getter without setter is not allowed. Consider implementing setter
	 * @return the value of publicField.
	 */
	@Accessor("publicField")
	public int getValue() {
		return value;
	}
	
	/* 
	@Accessor("publicField")
	public void throwExceptionOnCall(int value) throws Exception {
		throw new RuntimeException("example");
	}
	*/
	
}
