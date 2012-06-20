import java.lang.annotation.Documented;

import util.ExceptionExpectedOn;

import incubator.cfa.AccessorAnnotationWrapper;
import incubator.cfa.jdk.Accessor;

public class NEW2Sol12 {

	@AccessorAnnotationWrapper
	private @interface FieldAnnotation {
		OldFieldAnnotationForTesting value();
	}

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	private static Object inner_staticField = "FINAL VALUE";

	@Accessor("cause")
	@FieldAnnotation(value = @OldFieldAnnotationForTesting(a = {
			@Documented, @Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<NEW2SOL12>>>"))
	public Throwable getCause() {
		return inner_cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) {
		ExceptionExpectedOn.PUT.throwRTE();
	}

	@FieldAnnotation(value = @OldFieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD <<<NEW2SOL12>>>"))
	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		ExceptionExpectedOn.PUTSTATIC.throwRTE();
	}

}
