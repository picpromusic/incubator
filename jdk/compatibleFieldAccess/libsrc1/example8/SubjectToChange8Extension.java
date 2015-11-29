package example8;

import java.lang.reflect.Accessor;
import incubator.tests.SubjectToChange8;


public class SubjectToChange8Extension extends SubjectToChange8 {

	private int innerField;
	
	@Accessor("pField")
	public int getField() {
		return innerField;
	}
	
	@Accessor("pField")
	public void setField(int value) {
		innerField = Math.max(0, value);
	}

}
