import java.lang.annotation.Documented;

import incubator.FieldAnnotationForTesting; 
import java.lang.annotation.Documented;
import javalang.ref.AccessorAnnotationWrapper;

public class SubjectToChange {

	// This is the privately defined Annotation wrapper, mentioned below.
	// It is Annotated with an AccessorAnnotationWrapper to mark that this
	// is an annotation that originally belongs to an emulated old-field.
	// If we need multiple annotation-types in one class we must define
	// multiple of such annotations wrappers.
	@AccessorAnnotationWrapper
	private @interface PrivatelyDefindedFieldAnnotation {
		FieldAnnotationForTesting value();
	}

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	private static Object inner_staticField = "ORIG_VALUE_NEW";

	@PrivatelyDefindedFieldAnnotation(@FieldAnnotationForTesting(a = {
			@Documented, @Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<NEW3SOL12>>>"))
	@Accessor("cause")
	public Throwable getCause() {
		return inner_cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		throw new IllegalStateException("Not allowed to change");
	}
	
	@PrivatelyDefindedFieldAnnotation(@FieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD <<<NEW3SOL12>>>"))
	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		throw new RuntimeException("PutStatic not allowed anymore");
	}
}
