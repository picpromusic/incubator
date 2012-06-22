import java.lang.annotation.Documented;

import incubator.cfa.jdk.Accessor;
import incubator.cfa.jdk.AccessorAnnotationWrapper;

public class NEWSol2 {

	@AccessorAnnotationWrapper
	private @interface FACause {
		OldFieldAnnotationForTesting value();
	}

	private Throwable cause = new RuntimeException("INIT_NEW");
	private static Object staticField = "ORIG_VALUE_NEW";

	@Accessor("cause")
	@FACause(value = @OldFieldAnnotationForTesting(a = { @Documented,
			@Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<NEWSOL2>>>"))
	public Throwable getCause() {
		return cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) {
		this.cause = cause;
	}

	@FACause(value = @OldFieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD for <<<NEWSOL2>>>"))
	@Accessor("staticField")
	public static Object getFoo() {
		return staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		staticField = value;
	}

}
