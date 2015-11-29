package playground;

import java.lang.reflect.Accessor;

public class PlaygroundSubjectToChange {
	
	private double data;

	private static ThreadLocal<PlaygroundSubjectToChange> instance = new ThreadLocal<>();
	
	public static PlaygroundSubjectToChange getTLSingleton() {
		if (instance.get() == null) {
			instance.set(new PlaygroundSubjectToChange());
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
