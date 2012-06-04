
public class NEW {

	private Throwable cause = new RuntimeException("INIT_NEW");
	private static Object staticField = "ORIG_VALUE_NEW";

	
	@Accessor("cause")
	public Throwable getCause() {
		return cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		this.cause = cause;
	}
	
	@Accessor("staticField")
	public static Object getFoo() {
		return staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		staticField = value;
	}
	
}
