import java.lang.annotation.Documented;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class Test {
	private static boolean noReflectionPossible;
	private static boolean noAnnotationAvaiable;
	private static boolean putNotPossible;

	public static void main(String[] args) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		noReflectionPossible = Boolean.getBoolean("noReflectionPossible");
		noAnnotationAvaiable = Boolean.getBoolean("noAnnotationAvaiable");
		putNotPossible = Boolean.getBoolean("putNotPossible");

		if (args.length != 1) {
			throw new IllegalArgumentException("Usage "
					+ Test.class.getCanonicalName() + " [version]");
		}
		String version = args[0];

		testInstanceFieldGetAndPut(version);
		testStaticFieldGetAndPut(version);

		if (noReflectionPossible) {
			System.out
					.println("TestConfiguration: noReflectionPossible. Skip ReflectionTest");
		} else {
			testReflectiveInstanceFieldGetAndPut(version);
			testReflectiveStaticFieldGetAndPut(version);
		}

		System.out.println("Everything is fine");
	}

	private static void testInstanceFieldGetAndPut(String version)
			throws AssertionError {
		OLD oldRef = new OLD();

		// First we check if the initial value (cause.getMessage()) has the
		// expected Value. Just for Testing-purpose in this Testclass, every
		// version has another initial value. The Pattern for this ist
		// "INIT_"+version.
		if (!("INIT_" + version).equals(oldRef.cause.getMessage())) {
			throw new AssertionError(
					"Unexpected initial value for the cause instancefield:"
							+ Objects.toString(oldRef.cause)
							+ ". Expected: INIT_" + version);
		}

		// Now we try to change the value of the instancefield cause.
		try {
			oldRef.cause = new RuntimeException("NEW");
			// In some implementations this will fail, due to an exception
			// thrown by the given PUT-ACCESSOR-Method. If we expect an
			// exception and cannot catch one we will throw an AssertionError.
			if (putNotPossible)
				throw new AssertionError("Put should not be possible");
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable th) {
			// In some implementations the put will fail, due to an exception
			// thrown by
			// the given PUT-ACCESSOR-Method.
			if (!putNotPossible) {
				throw new AssertionError(
						"Put should be possible. Unexception Exception:" + th);
			}
		}
		// If anything was fine we check if the new value can be retrieved via
		// the cause instance field
		if (!putNotPossible && !"NEW".equals(oldRef.cause.getMessage())) {
			throw new AssertionError(
					"Unexpected new value for the cause instancefield, after an successful put: "
							+ Objects.toString(oldRef.cause)
							+ ". Expected: INIT_" + version);
		}
	}

	private static void testReflectiveInstanceFieldGetAndPut(String version)
			throws InstantiationException, IllegalAccessException,
			NoSuchFieldException, SecurityException {

		OLD oldRef = OLD.class.newInstance();

		Field instanceField = OLD.class.getField("cause");
		// The cause field is expected to be a non static field.
		if (Modifier.isStatic(instanceField.getModifiers())) {
			throw new AssertionError(
					"The instancefield cause should be non static");
		}

		// First we check if the initial value (cause.getMessage()) has the
		// expected Value. Just for Testing-purpose in this Testclass, every
		// version has another initial value. The Pattern for this ist
		// "INIT_"+version.
		String messageValue = ((Throwable) instanceField.get(oldRef))
				.getMessage();
		if (!("INIT_" + version).equals(messageValue)) {
			throw new AssertionError(
					"Unexpected initial value for the cause instancefield:"
							+ Objects.toString(messageValue)
							+ ". Expected: INIT_" + version);
		}

		// Now we try to change the value of the instancefield cause.
		try {
			instanceField.set(oldRef, new RuntimeException("NEW Reflective"));
			// In some implementations this will fail, due to an exception
			// thrown by the given PUT-ACCESSOR-Method. If we expect an
			// exception and cannot catch one we will throw an AssertionError.
			if (putNotPossible)
				throw new AssertionError("Put should not be possible");
		} catch (Throwable th) {
			// In some implementations the put will fail, due to an exception
			// thrown by the given PUT-ACCESSOR-Method.
			if (!putNotPossible) {
				throw new AssertionError(
						"Put should be possible. Unexception Exception:" + th);
			}
		}

		// If anything was fine we check if the new value can be retrieved via
		// the cause instance field
		messageValue = ((Throwable) instanceField.get(oldRef)).getMessage();
		if (!putNotPossible && !"NEW Reflective".equals(messageValue)) {
			throw new AssertionError(
					"Unexpected new value for the cause instancefield, after an successful put: "
							+ Objects.toString(messageValue)
							+ ". Expected: NEW Reflective");
		}

		if (noAnnotationAvaiable) {
			System.out
					.println("TestConfiguration: noAnnotationAvaiable. Skip AnnotationTest");

		} else {
			// The cause field in the OLD-Class has the following annotation.
			// @OldFieldAnnotationForTesting(
			// a = { @Documented, @Documented },
			// b = true,
			// s = "DEMO_NON-STATIC-FIELD for <<<OLD>>>"
			// )

			OldFieldAnnotationForTesting annotation = instanceField
					.getAnnotation(OldFieldAnnotationForTesting.class);
			if (annotation == null) {
				throw new AssertionError(
						"No OldFieldAnnotationForTesting on field cause found");
			}

			Documented[] a = annotation.a();
			// a = { @Documented, @Documented },
			if (a.length != 2) {
				throw new AssertionError(
						"OldFieldAnnotationForTesting.a should have a length of 2 for OLD.cause. But it is:"
								+ a.length + " -> " + Arrays.toString(a));
			}
			// a = { @Documented, @Documented },
			if (a[0] == null || a[1] == null) {
				throw new AssertionError(
						"The values of OldFieldAnnotationForTesting.a should be both non-null. But:  "
								+ a[0] + " " + a[1]);
			}

			// b = true,
			if (!annotation.b()) {
				throw new AssertionError(
						"The value of OldFieldAnnotationForTesting.b should be true");
			}

			// s = "DEMO_NON-STATIC-FIELD for <<<OLD>>>"
			if (!annotation.s().equals(
					"DEMO_NON-STATIC-FIELD for <<<" + version + ">>>")) {
				throw new AssertionError(
						"The value of OldFieldAnnotationForTesting.s should not be: "
								+ annotation.s() + ". Expected: "
								+ "DEMO_NON-STATIC-FIELD for <<<" + version
								+ ">>>");
			}
		}
	}

	private static void testStaticFieldGetAndPut(String version)
			throws AssertionError {
		// First we check if the initial value (OLD.staticField) has the
		// expected Value. Just for Testing-purpose in this Testclass, every
		// version has another initial value. The Pattern for this ist
		// "ORIG_VALUE_"+version.
		if (!("ORIG_VALUE_" + version).equals(OLD.staticField)) {
			throw new AssertionError(
					"Unexpected initial value for the staticField: "
							+ Objects.toString(OLD.staticField)
							+ ". Expected: ORIG_VALUE_" + version);
		}

		// Now we try to change the value of the staticField.
		try {
			OLD.staticField = new String("NEW Static");
			// In some implementations this will fail, due to an exception
			// thrown by the given PUT-ACCESSOR-Method. If we expect an
			// exception we throw an AssertionError if we cannot catch one.
			if (putNotPossible)
				throw new AssertionError("PutStatic should not be possible");
		} catch (Throwable th) {
			// In some implementations the put will fail, due to an exception
			// thrown by the given PUT-ACCESSOR-Method.
			if (!putNotPossible) {
				throw new AssertionError(
						"PutStatic should be possible. Unexception Exception:"
								+ th);
			}
		}
		// If anything was fine we check if the new value can be retrieved via
		// the
		// cause instance field
		if (!putNotPossible && !"NEW Static".equals(OLD.staticField)) {
			throw new AssertionError(
					"Unexpected new value for the staticField, after an successful PutStatic: "
							+ Objects.toString(OLD.staticField)
							+ ". Expected: NEW Static");
		}
	}

	private static void testReflectiveStaticFieldGetAndPut(String version)
			throws NoSuchFieldException, AssertionError, IllegalAccessException {
		Field staticField = OLD.class.getField("staticField");
		if (!Modifier.isStatic(staticField.getModifiers())) {
			throw new AssertionError();
		}
		if (!staticField.get(null).equals("NEW Static")) {
			throw new AssertionError(Objects.toString(staticField.get(null)));
		}

		if (noAnnotationAvaiable) {
			System.out
					.println("TestConfiguration: noAnnotationAvaiable. Skip AnnotationTest");

		} else {

			// The static in the OLD-Class has the following annotation.
			// @OldFieldAnnotationForTesting(
			// a = { @Documented },
			// b = false,
			// s = "DEMO_STATIC-FIELD for <<<OLD>>>"
			// )

			OldFieldAnnotationForTesting staticAnnotation = staticField
					.getAnnotation(OldFieldAnnotationForTesting.class);
			if (staticAnnotation == null) {
				throw new AssertionError(
						"No OldFieldAnnotationForTesting on staticField found");
			}

			Documented[] a = staticAnnotation.a();
			// a = { @Documented },
			if (a.length != 1) {
				throw new AssertionError(
						"OldFieldAnnotationForTesting.a should have a length of 1 for OLD.cause. But it is:"
								+ a.length + " -> " + Arrays.toString(a));
			}
			// a = { @Documented },
			if (a[0] == null) {
				throw new AssertionError(
						"The value of OldFieldAnnotationForTesting.a[0] should be non-null. But:  "
								+ a[0]);
			}

			// b = false,
			if (staticAnnotation.b()) {
				throw new AssertionError(
						"The value of OldFieldAnnotationForTesting.b should be false");
			}

			// s = "DEMO_STATIC-FIELD for <<<OLD>>>"
			if (!staticAnnotation.s().equals(
					"DEMO_STATIC-FIELD for <<<" + version + ">>>")) {
				throw new AssertionError(
						"The value of OldFieldAnnotationForTesting.s should not be: "
								+ staticAnnotation.s() + ". Expected: "
								+ "DEMO_STATIC-FIELD for <<<" + version + ">>>");
			}

		}
	}

}