package playground;

import java.lang.reflect.Accessor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlaygroundSubjectToChange {
	
	private double data;

	@Accessor(value="sField")
	public double getStatic() {
		return data;
	}

	@Accessor(value="sField")
	public void setStatic(double value) {
		data = value;
	}
	
}
