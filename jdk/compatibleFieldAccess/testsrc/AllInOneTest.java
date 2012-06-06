import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;

public class AllInOneTest {
	public static class TestDescription {

		private final Class<?> testClass;
		private final Class<?> classUnderTest;
		private final String testname;
		private final boolean testStatic;
		private final boolean testReflective;
		private final EnumSet<ExceptionExpectedOn> exceptionExpectedOn;
		private final boolean testNonStatic;

		public TestDescription(Class<?> testClass, Class<?> classUnderTest,
				String testname, boolean testStatic, boolean testNonStatic,
				boolean testReflective,
				ExceptionExpectedOn... exceptionExpectedOn) {
			this.testClass = testClass;
			this.classUnderTest = classUnderTest;
			this.testname = testname;
			this.testStatic = testStatic;
			this.testNonStatic = testNonStatic;
			this.testReflective = testReflective;
			this.exceptionExpectedOn = EnumSet
					.noneOf(ExceptionExpectedOn.class);
			for (ExceptionExpectedOn ex : exceptionExpectedOn) {
				this.exceptionExpectedOn.add(ex);
			}
		}

		public Class<?> getTestClass() {
			return testClass;
		}

		public Class<?> getClassUnderTest() {
			return classUnderTest;
		}

		public String getTestname() {
			return testname;
		}

		public boolean isTestStatic() {
			return testStatic;
		}

		public boolean isTestReflective() {
			return testReflective;
		}

		public EnumSet<ExceptionExpectedOn> getExceptionExpectedOn() {
			return exceptionExpectedOn;
		}

		public boolean isTestNonStatic() {
			return testNonStatic;
		}

	}

	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		System.out.println("Version 2012-06-05 05:40");
		boolean reflect = true;
		boolean testStatic = true;
		boolean testNonStatic = true;

		List<TestDescription> tests = new ArrayList<TestDescription>();
		tests.add(new TestDescription(TestOld.class, OLD.class, "<<<OLD>>>",
				testStatic, testNonStatic, reflect));
		tests.add(new TestDescription(TestNewSolution2.class, NEWSol2.class,
				"<<<NEWSOL2>>>", testStatic, testNonStatic, reflect));
		tests.add(new TestDescription(TestNew2Solution12.class,
				NEW2Sol12.class, "<<<NEW2SOL12>>>", testStatic, testNonStatic,
				reflect, ExceptionExpectedOn.PUT, ExceptionExpectedOn.PUTSTATIC));

		for (TestDescription testDescription : tests) {
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

	private static void testit(TestDescription desc) {
		for (int i = 0; i < TIMES; i++) {
			System.out.println(desc.getTestname());
			if (desc.isTestNonStatic()) {
				try {
					desc.getTestClass().getMethod("testIt").invoke(null);
					if (desc.getExceptionExpectedOn().contains(
							ExceptionExpectedOn.GET)
							|| desc.getExceptionExpectedOn().contains(
									ExceptionExpectedOn.PUT)) {
						throw new RuntimeException("Exception expected");
					}
				} catch (InvocationTargetException e) {
					Throwable target = e.getTargetException();
					ExceptionExpectedOn why;
					try {
						why = (ExceptionExpectedOn) target.getClass()
								.getMethod("getWhy").invoke(target);
						if (desc.getExceptionExpectedOn().contains(why)) {
							System.out.println("Everything is fine: ***" + why);
						} else {
							throw new RuntimeException(e);
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| SecurityException e1) {
						throw new RuntimeException(e);
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e);
				}
			}
			if (desc.isTestStatic()) {
				try {
					desc.getTestClass().getMethod("testItStatically")
							.invoke(null);
					if (desc.getExceptionExpectedOn().contains(
							ExceptionExpectedOn.GETSTATIC)
							|| desc.getExceptionExpectedOn().contains(
									ExceptionExpectedOn.PUTSTATIC)) {
						throw new RuntimeException("Exception expected");
					}
				} catch (InvocationTargetException e) {
					Throwable target = e.getTargetException();
					ExceptionExpectedOn why;
					try {
						why = (ExceptionExpectedOn) target.getClass()
								.getMethod("getWhy").invoke(target);
						if (desc.getExceptionExpectedOn().contains(why)) {
							System.out.println("Everything is fine: ***" + why);
						} else {
							throw new RuntimeException(e);
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| SecurityException e1) {
						throw new RuntimeException(e);
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e);
				}
			}
			if (desc.isTestReflective()) {
				System.out.println("<<<REFLECTIVE>>>" + desc.getTestname());
				for (int j = 0; i < TIMES; i++) {
					if (desc.isTestNonStatic()) {
						try {
							Object instance = desc.getClassUnderTest()
									.newInstance();
							Field field = desc.getClassUnderTest().getField(
									"cause");
							System.out.println(field.get(instance));
							RuntimeException newCause = new RuntimeException(
									"Reflective");
							field.set(instance, newCause);
							System.out.println(field.get(instance));
							if (desc.getExceptionExpectedOn().contains(
									ExceptionExpectedOn.GET)
									|| desc.getExceptionExpectedOn().contains(
											ExceptionExpectedOn.PUT)) {
								throw new RuntimeException("Exception expected");
							}
						} catch (InstantiationException
								| IllegalAccessException | NoSuchFieldException
								| SecurityException e) {
							throw new RuntimeException(e);
						} catch (Throwable th) {
							ExceptionExpectedOn why;
							try {
								why = (ExceptionExpectedOn) th.getClass()
										.getMethod("getWhy").invoke(th);
							} catch (IllegalAccessException
									| IllegalArgumentException
									| InvocationTargetException
									| NoSuchMethodException | SecurityException e1) {
								throw new RuntimeException(th);
							}
							if (desc.getExceptionExpectedOn().contains(why)) {
								System.out.println("Everything is fine: ***"
										+ th);
							} else {
								throw new RuntimeException(th);
							}
						}

					}
					if (desc.isTestStatic()) {
						try {
							Field field = desc.getClassUnderTest().getField(
									"staticField");
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
							ExceptionExpectedOn why;
							try {
								why = (ExceptionExpectedOn) th.getClass()
										.getMethod("getWhy").invoke(th);
							} catch (IllegalAccessException
									| IllegalArgumentException
									| InvocationTargetException
									| NoSuchMethodException | SecurityException e1) {
								throw new RuntimeException(th);
							}
							if (desc.getExceptionExpectedOn().contains(why)) {
								System.out.println("Everything is fine: ***"
										+ th);
							} else {
								throw new RuntimeException(th);
							}
						}
					}
				}
			}
		}
	}
}
