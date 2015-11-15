package example8;

import javalang.ref.Accessor;
import incubator.tests.SubjectToChange8;


public class SubjectToChange8Extension extends SubjectToChange8 {

	private int pField;
	
	@Accessor("pField")
	public int getField() {
		return pField;
	}
	
	@Accessor("pField")
	public void setField(int value) {
		pField = Math.max(0, value);
	}

}
