package playground;

import javalang.ref.Accessor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlaygroundSubjectToChange {
	
	private double data;

	private static Map<Object,PlaygroundSubjectToChange> instances = new ConcurrentHashMap<>();
	
	public static PlaygroundSubjectToChange getSingleton(@Callee Object callee) {
		PlaygroundSubjectToChange instance = instances.get(callee);
		if (instance == null) {
			synchronized (instances) {
				instance = instances.get(callee);
				if (instance == null) {
					instance = new PlaygroundSubjectToChange();
					instances.put(callee,instance);
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
