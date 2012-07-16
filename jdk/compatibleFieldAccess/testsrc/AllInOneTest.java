import incubator.cfa.jdk.WrappedCheckedCompatiblityException;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import util.DescriptionOfTest;
import util.DescriptionOfTest.TestDescriptionBuilder;
import util.ExceptionExpectedOn;
import util.ExceptionExpectedOn.CHECKED;
import util.ExceptionExpectedOn.RUNTIME;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		System.out.println("Version 2012-06-05 05:40");

		// True if we want to test the reflective functionality
		// Because it is not implemented yet, we switch this off
		boolean reflect = false;

		// True if we want to test the static and/or the nonStatic functionality
		boolean testStatic = true;
		boolean testNonStatic = true;

		List<DescriptionOfTest> tests = new ArrayList<DescriptionOfTest>();

		// Define a Test that is called <<<OLD>>> that tests the implementation
		// class OLD via the testing class TestOld.
		TestDescriptionBuilder old = DescriptionOfTest.createNew("<<<OLD>>>")
				.forClass(TestOld.class).testing(OLD.class);

		// Define a Test that is called <<<NEWSOL2>>> that tests the
		// implementation class NEWSOL2 via the testing class TestNewSolution2.
		// This Test is only capable for Solution2.
		TestDescriptionBuilder newSol2 = DescriptionOfTest
				.createNew("<<<NEWSOL2>>>").forClass(TestNewSolution2.class)
				.testing(NEWSol2.class);

		// Define a Test that is called <<<NEW2SOL12>>> that tests the
		// implementation class NEW2SOL12 via the testing class
		// TestNew2Solution12. This Test is capable for Solution 1 and 2.
		// It is expected to run into an RuntimeException while executing the
		// PUT Instruction for the static and the non-static field.
		TestDescriptionBuilder new2Sol12 = DescriptionOfTest
				.createNew("<<<NEW2SOL12>>>")
				.forClass(TestNew2Solution12.class).testing(NEW2Sol12.class)
				.runtimeExceptionOn(ExceptionExpectedOn.PUT)
				.runtimeExceptionOn(ExceptionExpectedOn.PUTSTATIC);

		// Define a Test that is called <<<NEW3SOL12>>> that tests the
		// implementation class NEW3SOL12 via the testing class
		// TestNew3Solution12. This Test is capable for Solution 1 and 2.
		// It is expected to run into an CheckedException while executing the
		// PUT Instruction for the static and the non-static field.
		// The NEW3SOL12 is also capable for an reflective test of the
		// annotations
		TestDescriptionBuilder new3Sol12 = DescriptionOfTest
				.createNew("<<<NEW3SOL12>>>")
				.forClass(TestNew3Solution12.class).testing(NEW3Sol12.class)
				.checkedExceptionOn(ExceptionExpectedOn.PUT)
				.checkedExceptionOn(ExceptionExpectedOn.PUTSTATIC)
				.checkAnnotation();

		// Prepare the above defined Tests to turn on the features
		// "testStatic","testNonStatic" and "reflection" add them all to the
		// lists of to be executed tests.
		List<TestDescriptionBuilder> sentences = new ArrayList<>();
		sentences.add(old);
		sentences.add(newSol2);
		sentences.add(new2Sol12);
		sentences.add(new3Sol12);

		for (TestDescriptionBuilder sentence : sentences) {
			if (testStatic) {
				sentence.testStatic();
			}
			if (testNonStatic) {
				sentence.testNonStatic();
			}
			if (reflect) {
				sentence.testReflective();
			}

			// Add to the list of to be executed tests.
			sentence.testIt(tests);
		}

		// Execute each test
		for (DescriptionOfTest testDescription : tests) {
			try {
				testit(testDescription);
			} catch (Throwable th) {
				System.err.println(testDescription.getTestname() + " failed");
				th.printStackTrace();
				System.err.flush();
			} finally {
				System.out.flush();
			}
		}
	}

	/**
	 * Execcutes the Test described by the DescriptionOfTest
	 * 
	 * @param desc
	 */
	private static void testit(DescriptionOfTest desc) {
		for (int i = 0; i < TIMES; i++) {
			System.out.println(desc.getTestname());
			// Execute the non reflective Tests.
			if (desc.isTestNonStatic()) {
				testNonStatic(desc);
			}
			if (desc.isTestStatic()) {
				testStatic(desc);
			}
			// Execute the refective tests.
			if (desc.isTestReflective()) {
				System.out.println("<<<REFLECTIVE>>>" + desc.getTestname());
				for (int j = 0; i < TIMES; i++) {
					if (desc.isTestNonStatic()) {
						testNonStaticReflective(desc);
					}
					if (desc.isTestStatic()) {
						testStaticReflective(desc);
					}
				}
			}
		}
	}

	/**
	 * Accesses the static staticField-Field of the Class under Test. It checks
	 * the annoations( if this test is capable for annotation tests) and
	 * retrieves the containing value. Then it initializes a new value for the
	 * cause-Field and sets the new value. Finaly It checks if an exception
	 * occured and if it matches the expectation
	 * "exceptionExpectedOnStaticAccess" of the given testdescription and checks
	 * if this matches the expected exception cause.
	 * 
	 * @param desc
	 *            The Description of the Test.
	 */
	private static void testStaticReflective(DescriptionOfTest desc) {
		try {
			Field field = desc.getClassUnderTest().getField("staticField");
			testAnnotationOfField(desc, field, true);
			System.out.println(field.get(null));
			field.set(null, "NEW_VALUE");
			System.out.println(field.get(null));
			if (desc.getRuntimeExceptionExpectedOn().contains(
					ExceptionExpectedOn.GETSTATIC)
					|| desc.getRuntimeExceptionExpectedOn().contains(
							ExceptionExpectedOn.PUTSTATIC)) {
				throw new RuntimeException("Exception expected");
			}
		} catch (IllegalAccessException | NoSuchFieldException
				| SecurityException e) {
			throw new RuntimeException(e);
		} catch (Throwable th) {
			checkExceptionExpectation(desc, th);
		}
	}

	private static void checkExceptionExpectation(DescriptionOfTest desc,
			Throwable th) {
		th.printStackTrace();
		boolean runtimeExceptionExpected = isRuntime(th);
		ExceptionExpectedOn why = extractWhy(th);
		// Check if the why parameter matches the expected
		// Exceptioncause.
		if (desc.getRuntimeExceptionExpectedOn().contains(why)) {
			System.out.println("Everything is fine: ***" + th);
		} else if (desc.getCheckedExceptionExpectedOn().contains(why)) {
			System.out.println("Everything is fine: ***" + th);
		} else {
			throw new RuntimeException(th);
		}
	}

	private static boolean isRuntime(Throwable th) {
		return th instanceof RUNTIME;
	}

	/**
	 * Accesses the non static cause-Field of the Class under Test. It checks
	 * the annoations( if this test is capable for annotation tests) and
	 * retrieves the containing value. Then it initializes a new value for the
	 * cause-Field and sets the new value. Finaly It checks if an exception
	 * occured and if it matches the expectation
	 * "exceptionExpectedOnStaticAccess" of the given testdescription and checks
	 * if this matches the expected exception cause.
	 * 
	 * @param desc
	 *            The Description of the Test.
	 */
	private static void testNonStaticReflective(DescriptionOfTest desc) {
		try {
			Object instance = desc.getClassUnderTest().newInstance();
			Field field = desc.getClassUnderTest().getField("cause");
			testAnnotationOfField(desc, field, false);
			System.out.println(field.get(instance));
			RuntimeException newCause = new RuntimeException("Reflective");
			field.set(instance, newCause);
			System.out.println(field.get(instance));
			if (desc.exceptionExpectedOnNonStaticAccess()) {
				throw new RuntimeException("Exception expected");
			}
		} catch (InstantiationException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		} catch (Throwable th) {
			checkExceptionExpectation(desc, th);
		}

	}

	private static void testAnnotationOfField(DescriptionOfTest desc,
			Field field, boolean staticFieldVariant) {
		if (desc.isAnnoationTestCapable()) {
			Annotation[] annotations = field.getAnnotations();
			if (annotations.length == 1) {
				OldFieldAnnotationForTesting anno = (OldFieldAnnotationForTesting) annotations[0];
				// String
				String sValue = anno.s();
				String expectedValue = "DEMO_"
						+ (staticFieldVariant ? "STATIC-FIELD"
								: "NON-STATIC-FIELD") + " for "
						+ desc.getTestname();
				if (!sValue.equals(expectedValue)) {
					throw new RuntimeException(
							"Unexpected Value in AnnotationParameter s "
									+ sValue);
				}
				boolean bValue = anno.b();
				if (bValue == staticFieldVariant) {
					throw new RuntimeException(
							"Unexpected Value in AnnotationParameter b "
									+ bValue);
				}
				Documented[] aValue = anno.a();
				if (aValue.length != (staticFieldVariant ? 1 : 2)) {
					throw new RuntimeException(
							"Unexpected Size of Array in AnnotationParameter a "
									+ bValue);
				}
				if (!aValue[0].annotationType().equals(Documented.class)) {
					throw new RuntimeException(
							"Unexpected Annotationtype in AnnotationParameter a[0] "
									+ aValue[0].annotationType());
				}
				if (!staticFieldVariant
						&& !aValue[1].annotationType().equals(Documented.class)) {
					throw new RuntimeException(
							"Unexpected Annotationtype in AnnotationParameter a[1] "
									+ aValue[1].annotationType());
				}
				System.out.println("Everything is fine with the annoatation");
			} else {
				throw new RuntimeException(
						"Each field in this test needs to be annotated by 1 annotation.");
			}
		}
	}

	/**
	 * Calls the static testItStatically Method of the Class that is returned by
	 * desc.getTestClass(). The TestIt method is generated via the method
	 * GEN.genStaticTest. It tests if an exception occured and if this matches
	 * the expectation "exceptionExpectedOnStaticAccess" of the given
	 * testdescription and checks if this matches the expected exception cause.
	 * 
	 * @param desc
	 *            The Description of the Test.
	 */
	private static void testStatic(DescriptionOfTest desc) {
		try {
			desc.getTestClass().getMethod("testItStatically").invoke(null);
			if (desc.exceptionExpectedOnStaticAccess()) {
				throw new RuntimeException("Exception expected");
			}
		} catch (InvocationTargetException e) {
			// Because we are using Reflection here we need to "unpack"
			// the real Exception.
			checkExceptionExpectation(desc, e.getTargetException());
		} catch (IllegalAccessException | IllegalArgumentException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Calls the static testIt Method of the Class that is returned by
	 * desc.getTestClass(). The TestIt method is generated via the method
	 * GEN.genNonStaticTest. It tests if an exception occured and if this
	 * matches the expectation "exceptionExpectedOnNonStaticAccess" of the given
	 * testdescription and checks if this matches the expected exception cause.
	 * 
	 * @param desc
	 *            The Description of the Test.
	 */
	private static void testNonStatic(DescriptionOfTest desc) {
		try {
			desc.getTestClass().getMethod("testIt").invoke(null);
			if (desc.exceptionExpectedOnNonStaticAccess()) {
				throw new RuntimeException("Exception expected");
			}
		} catch (InvocationTargetException e) {
			// Because we are using Reflection here we need to "unpack"
			// the real Exception.
			checkExceptionExpectation(desc, e.getTargetException());
		} catch (IllegalAccessException | IllegalArgumentException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Extracts the why value out of the two expected Exceptiontypes
	 * ExceptionExpectedOn.RUNTIME or ExceptionExpectedOn.CHECKED
	 * 
	 * @param th
	 *            Throwable that should be either RUNTIME or CHECKED of
	 *            ExceptionExpectedOn
	 * @param runtimeExceptionExpected
	 * @return the Result of the Methodcall getWhy() on the RUNTIME or CHECK
	 *         Exception.
	 * @throws ClassCastException
	 *             when th is neither ExceptionExpectedOn.RUNTIME nor
	 *             ExceptionExpectedOn.CHECKED
	 */
	private static ExceptionExpectedOn extractWhy(Throwable th)
			throws ClassCastException {
		// The only expected Exceptiontypes are
		// ExceptionExpectedOn.RUNTIME or ExceptionExpectedOn.CHECKED
		if (isRuntime(th)) {
			RUNTIME ex = (RUNTIME) th;
			return ex.getWhy();
		} else {
			// As of 4.7.5 of JVMS, we should only expect to catch an
			// RuntimeException.
			// WrappedCheckedCompatiblityException wrapper =
			// (WrappedCheckedCompatiblityException) th;
			// CHECKED ex = (CHECKED) wrapper.getCause();
			// But for now we accept the CheckedException as well.
			CHECKED ex = (CHECKED) th;
			return ex.getWhy();
		}
	}

}
