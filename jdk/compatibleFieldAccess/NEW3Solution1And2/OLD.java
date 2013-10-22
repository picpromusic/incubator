import java.lang.annotation.Documented;

import incubator.cfa.jdk.Accessor;
import incubator.cfa.jdk.AccessorAnnotationWrapper;

public class OLD {

	// This is the privately defined Annotation wrapper, mentioned below.
	// It is Annotated with an AccessorAnnotationWrapper to mark that this
	// is an annotation that originally belongs to an emulated old-field.
	// If we need multiple annotation-types in one class we must define
	// multiple of such annotations wrappers.
	@AccessorAnnotationWrapper
	private @interface PrivatelyDefindedFieldAnnotation {
		OldFieldAnnotationForTesting value();
	}

	private Throwable inner_cause = new RuntimeException("INIT_NEW3");
	private static Object inner_staticField = "ORIG_VALUE_NEW3";

	// Because we cannot annotate the Method with the annotation
	// OldFieldAnnotationForTesting we must wrap the Annotation with an
	// privately defined Annotations (here: PrivatelyDefindedFieldAnnotation)
	// that wraps the needed Annotation.
	@Accessor("cause")
	@PrivatelyDefindedFieldAnnotation(@OldFieldAnnotationForTesting(a = {
			@Documented, @Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<NEW3SOL12>>>"))
	public Throwable getCause() {
		return inner_cause;
	}

	@Accessor("cause")
	public void initCause(Throwable cause) throws Throwable {
		throw new Exception("Put not allowed anymore");
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
	public static void setFoo(Object value) throws Throwable {
		throw new Exception("PutStatic not allowed anymore");
	}

}
