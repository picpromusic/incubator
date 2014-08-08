package playground;

import javalang.ref.Accessor;

public class PlaygroundSubjectToChange {
	private static int field;
	private static double sField;

	@Accessor("sField")
	public static double getStatic() {
		return sField;
	}

	@Accessor("sField")
	public static void setStatic(double value) {
		sField = value;
	}
	
	@Accessor("field")
	public static void setVar(int value) {
		field = value;
	}
	
	@Accessor("field")
	public static int getVar() {
		return field;
	}
	
}
