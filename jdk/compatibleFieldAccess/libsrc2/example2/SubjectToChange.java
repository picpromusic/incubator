package example2;

import java.lang.reflect.Accessor;

public class SubjectToChange {
	private int publicField;
	
	public SubjectToChange(int value) {
		this.publicField = value;
	}
	
	/**
	 * Getter without setter is not allowed. Consider implementing setter
	 * @return the value of publicField.
	 */
	@Accessor("publicField")
	public int getPublicField() {
		return publicField;
	}
	
	/* 
	@Accessor("publicField")
	public void throwExceptionOnCall(int value) throws Exception {
		throw new RuntimeException("example");
	}
	*/
	
}
