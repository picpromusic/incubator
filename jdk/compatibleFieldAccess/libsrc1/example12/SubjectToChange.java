package example12;

import java.lang.reflect.Accessor;

public class SubjectToChange {
	
	private double data;

	private static ThreadLocal<SubjectToChange> instance = new ThreadLocal<>();
	
	public static SubjectToChange getTLSingleton() {
		if (instance.get() == null) {
			instance.set(new SubjectToChange());
		}
		return instance.get();
	}
	
	@Accessor(value="sField",instanceFactory="getTLSingleton")
	public double getStatic() {
		return data;
	}

	@Accessor(value="sField",instanceFactory="getTLSingleton")
	public void setStatic(double value) {
		data = value;
	}
	
}
