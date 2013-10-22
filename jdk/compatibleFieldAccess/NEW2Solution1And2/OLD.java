import java.lang.annotation.Documented;

import incubator.cfa.jdk.Accessor;

public class OLD {

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	private static Object inner_staticField = "ORIG_VALUE_NEW2";

	@Accessor("cause")
	public Throwable getCause() {
		return inner_cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) {
		throw new RuntimeException("Put not allowed anymore");
	}

	// Because we cannot annotate the Method with the annotation
	// OldFieldAnnotationForTesting we must wrap the Annotation with an
	// privately defined Annotations (here: PrivatelyDefindedFieldAnnotation)
	// that wraps the needed Annotation.
	@PrivatelyDefindedFieldAnnotation(@OldFieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD <<<NEW3SOL12>>>"))
	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		throw new RuntimeException("PutStatic not allowed anymore");
	}

}
