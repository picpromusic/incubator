import java.lang.annotation.Documented;

import util.ExceptionExpectedOn;

import incubator.cfa.jdk.Accessor;
import incubator.cfa.jdk.AccessorAnnotationWrapper;

public class NEW3Sol12 {

	@AccessorAnnotationWrapper
	private @interface FieldAnnotation {
		OldFieldAnnotationForTesting value();
	}

	private Throwable inner_cause = new RuntimeException("INIT_NEW3");
	private static Object inner_staticField = "FINAL VALUE";

	@Accessor("cause")
	@FieldAnnotation(value = @OldFieldAnnotationForTesting(a = {
			@Documented, @Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<NEW3SOL12>>>"))
	public Throwable getCause() {
		return inner_cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) throws Throwable {
		ExceptionExpectedOn.PUT.throwCHECKED();
	}

	@FieldAnnotation(value = @OldFieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD <<<NEW3SOL12>>>"))
	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) throws Throwable {
		ExceptionExpectedOn.PUTSTATIC.throwCHECKED();
	}

}
