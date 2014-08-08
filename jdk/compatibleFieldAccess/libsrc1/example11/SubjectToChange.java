package example11;

import javalang.ref.Accessor;

public class SubjectToChange {
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

	/**
	 * It may be a good idea, it may be a bad idea. To support this. However
	 * this prototype allow changes from non-static to static. Nevertheless the
	 * same effect can always achieved by providing a non-static method that simply
	 * provides static-like access to the field. 
	 * 
	 * @param value
	 */
	@Accessor(value="field",allowNonStaticAccess=true)
	public static void setVar(int value) {
//	public        void setVar(int value) {
		field = value;
	}

	/**
	 * It may be a good idea, it may be a bad idea. To support this. However
	 * this prototype allow changes from non-static to static. Nevertheless the
	 * same effect can always achieved by providing a non-static method that simply
	 * provides static-like access to the field. 
	 *
	 * @return the value
	 */
	@Accessor(value="field",allowNonStaticAccess=true)
	public static int getVar() {
//  public        int getVar() {
		return field;
	}

}
