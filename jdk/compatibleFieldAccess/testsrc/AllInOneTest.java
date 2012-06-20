import incubator.cfa.AccessorAnnotationWrapper;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import util.DescriptionOfTest;
import util.DescriptionOfTest.DescriptionOfTestSentence;
import util.ExceptionExpectedOn;
import util.ExceptionExpectedOn.CHECKED;
import util.ExceptionExpectedOn.RUNTIME;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		System.out.println("Version 2012-06-05 05:40");
		boolean reflect = true;
		boolean testStatic = true;
		boolean testNonStatic = true;

		List<DescriptionOfTest> tests = new ArrayList<DescriptionOfTest>();

		DescriptionOfTestSentence old = DescriptionOfTest
				.createNew("<<<OLD>>>").forClass(TestOld.class)
				.testing(OLD.class);

		DescriptionOfTestSentence newSol2 = DescriptionOfTest
				.createNew("<<<NEWSOL2>>>").forClass(TestNewSolution2.class)
				.testing(NEWSol2.class);

		DescriptionOfTestSentence new2Sol12 = DescriptionOfTest
				.createNew("<<<NEW2SOL12>>>")
				.forClass(TestNew2Solution12.class).testing(NEW2Sol12.class)
				.exceptionOn(ExceptionExpectedOn.PUT)
				.exceptionOn(ExceptionExpectedOn.PUTSTATIC);

		List<DescriptionOfTestSentence> sentences = new ArrayList<>();
		sentences.add(old);
		sentences.add(newSol2);
		sentences.add(new2Sol12);

		for (DescriptionOfTestSentence sentence : sentences) {
			if (testStatic) {
				sentence.testStatic();
				newSol2.testStatic();
			}
			if (testNonStatic) {
				sentence.testNonStatic();
			}
			if (reflect) {
				sentence.testReflective();
			}

			sentence.testIt(tests);
		}

		// tests.add(new DescriptionOfTest(TestOld.class, OLD.class,
		// "<<<OLD>>>",
		// testStatic, testNonStatic, reflect));
		// tests.add(new DescriptionOfTest(TestNewSolution2.class,
		// NEWSol2.class,
		// "<<<NEWSOL2>>>", testStatic, testNonStatic, reflect));
		// tests.add(new DescriptionOfTest(TestNew2Solution12.class,
		// NEW2Sol12.class, "<<<NEW2SOL12>>>", testStatic, testNonStatic,
		// reflect, ExceptionExpectedOn.PUT, ExceptionExpectedOn.PUTSTATIC));

		for (DescriptionOfTest testDescription : tests) {
			try {
				testit(testDescription);
			} catch (Throwable th) {
				System.err.println("TestOld failed");
				th.printStackTrace();
			} finally {
				System.out.flush();
			}
		}
	}

	private static void testit(DescriptionOfTest desc) {
		for (int i = 0; i < TIMES; i++) {
			System.out.println(desc.getTestname());
			if (desc.isTestNonStatic()) {
				testNonStatic(desc);
			}
			if (desc.isTestStatic()) {
				testStatic(desc);
			}
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

	private static void testStaticReflective(DescriptionOfTest desc) {
		try {
			Field field = desc.getClassUnderTest().getField("staticField");
			System.out.println(field.get(null));
			field.set(null, "NEW_VALUE");
			System.out.println(field.get(null));
			if (desc.getExceptionExpectedOn().contains(
					ExceptionExpectedOn.GETSTATIC)
					|| desc.getExceptionExpectedOn().contains(
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
		ExceptionExpectedOn why = extractWhy(th);
		// Check if the why parameter matches the expected
		// Exceptioncause.
		if (desc.getExceptionExpectedOn().contains(why)) {
			System.out.println("Everything is fine: ***" + th);
		} else {
			throw new RuntimeException(th);
		}
	}

	private static void testNonStaticReflective(DescriptionOfTest desc) {
		try {
			Object instance = desc.getClassUnderTest().newInstance();
			Field field = desc.getClassUnderTest().getField("cause");
			System.out.println(field.get(instance));

			Annotation[] annotations = field.getAnnotations();
			if (annotations.length == 1) {
				OldFieldAnnotationForTesting anno = (OldFieldAnnotationForTesting) annotations[0];
				// String
				String sValue = anno.s();
				if (!sValue.equals("DEMO_NON-STATIC-FIELD for "
						+ desc.getTestname())) {
					throw new RuntimeException(
							"Unexpected Value in AnnotationParameter s "
									+ sValue);
				}
				boolean bValue = anno.b();
				if (!bValue) {
					throw new RuntimeException(
							"Unexpected Value in AnnotationParameter b "
									+ bValue);
				}
				Documented[] aValue = anno.a();
				if (aValue.length != 2) {
					throw new RuntimeException(
							"Unexpected Size of Array in AnnotationParameter a "
									+ bValue);
				}
				if (!aValue[0].annotationType().equals(Documented.class)) {
					throw new RuntimeException(
							"Unexpected Annotationtype in AnnotationParameter a[0] "
									+ aValue[0].annotationType());
				}
				if (!aValue[1].annotationType().equals(Documented.class)) {
					throw new RuntimeException(
							"Unexpected Annotationtype in AnnotationParameter a[1] "
									+ aValue[1].annotationType());
				}
				System.out.println("Everything is fine with the annoatation");
			} else {
				throw new RuntimeException(
						"Each field in this test needs to be annotated by 1 annotation.");
			}

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
	 *            The Description of the Test. This Meth
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
		if (th instanceof RUNTIME) {
			RUNTIME ex = (RUNTIME) th;
			return ex.getWhy();
		} else {
			CHECKED ex = (CHECKED) th;
			return ex.getWhy();
		}
	}

}
