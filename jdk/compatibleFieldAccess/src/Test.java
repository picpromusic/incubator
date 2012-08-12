import java.lang.annotation.Documented;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import org.objectweb.asm.commons.StaticInitMerger;

public class Test {
	public static void main(String[] args) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		OLD oldRef = new OLD();
		if (!oldRef.cause.getMessage().equals("INIT_OLD")) {
			throw new AssertionError(Objects.toString(oldRef.cause));
		}
		oldRef.cause = new RuntimeException("NEW");
		if (!oldRef.cause.getMessage().equals("NEW")) {
			throw new AssertionError(Objects.toString(oldRef.cause));
		}

		if (!oldRef.staticField.equals("ORIG_VALUE")) {
			throw new AssertionError(Objects.toString(oldRef.staticField));
		}

		OLD.staticField = new String("NEW Static");
		if (!oldRef.staticField.equals("NEW Static")) {
			throw new AssertionError(Objects.toString(oldRef.staticField));
		}

		oldRef = OLD.class.newInstance();
		Field instanceField = OLD.class.getField("cause");
		if (Modifier.isStatic(instanceField.getModifiers())) {
			throw new AssertionError();
		}
		if (!((Throwable) instanceField.get(oldRef)).getMessage().equals(
				"INIT_OLD")) {
			throw new AssertionError(
					Objects.toString(instanceField.get(oldRef)));
		}
		instanceField.set(oldRef, new RuntimeException("NEW Reflective"));
		if (!((Throwable) instanceField.get(oldRef)).getMessage().equals(
				"NEW Reflective")) {
			throw new AssertionError(
					Objects.toString(instanceField.get(oldRef)));
		}

		OldFieldAnnotationForTesting annotation = instanceField
				.getAnnotation(OldFieldAnnotationForTesting.class);
		Documented[] a = annotation.a();
		if (a.length != 2) {
			throw new AssertionError(a.length);
		}
		if (a[0] == null || a[1] == null) {
			throw new AssertionError(a[0] + " " + a[1]);
		}
		if (!annotation.b()) {
			throw new AssertionError();
		}
		if (!annotation.s().equals("DEMO_NON-STATIC-FIELD for <<<OLD>>>")) {
			throw new AssertionError(annotation.s());
		}

		Field staticField = OLD.class.getField("staticField");
		if (!Modifier.isStatic(staticField.getModifiers())) {
			throw new AssertionError();
		}
		if (!staticField.get(null).equals("NEW Static")) {
			throw new AssertionError(
					Objects.toString(staticField.get(null)));
		}
		instanceField.set(oldRef, new RuntimeException("NEW Reflective"));
		if (!((Throwable) instanceField.get(oldRef)).getMessage().equals(
				"NEW Reflective")) {
			throw new AssertionError(
					Objects.toString(instanceField.get(oldRef)));
		}

		OldFieldAnnotationForTesting staticAnnotation = staticField
				.getAnnotation(OldFieldAnnotationForTesting.class);
		a = staticAnnotation.a();
		if (a.length != 1) {
			throw new AssertionError(a.length);
		}
		if (a[0] == null ) {
			throw new AssertionError(a[0]);
		}
		if (staticAnnotation.b()) {
			throw new AssertionError();
		}
		if (!staticAnnotation.s().equals("DEMO_STATIC-FIELD for <<<OLD>>>")) {
			throw new AssertionError(staticAnnotation.s());
		}

	}
}
