package example12;

import java.lang.reflect.Accessor;

public class SubjectToChange {
	
	private double data;

	private static SubjectToChange instance;
	
	public static SubjectToChange getSingleton() {
		if (instance == null) {
			synchronized (SubjectToChange.class) {
				if (instance == null) {
					instance = new SubjectToChange();
				}
			}
		}
		return instance;
	}
	
	@Accessor(value="sField",instanceFactory="getSingleton")
	public double getStatic() {
		return data;
	}

	@Accessor(value="sField",instanceFactory="getSingleton")
	public void setStatic(double value) {
		data = value;
	}
	
}
