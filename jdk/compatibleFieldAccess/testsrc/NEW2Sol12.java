import java.lang.annotation.Documented;

import util.ExceptionExpectedOn;

import incubator.cfa.jdk.Accessor;
import incubator.cfa.jdk.AccessorAnnotationWrapper;

public class NEW2Sol12 {

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	private static Object inner_staticField = "FINAL VALUE";

	@Accessor("cause")
	public Throwable getCause() {
		return inner_cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) {
		ExceptionExpectedOn.PUT.throwRTE();
	}

	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		ExceptionExpectedOn.PUTSTATIC.throwRTE();
	}

}
