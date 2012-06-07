import java.util.EnumSet;

public class TestDescription {

	private final Class<?> testClass;
	private final Class<?> classUnderTest;
	private final String testname;
	private final boolean testStatic;
	private final boolean testReflective;
	private final EnumSet<ExceptionExpectedOn> exceptionExpectedOn;
	private final boolean testNonStatic;

	public TestDescription(Class<?> testClass, Class<?> classUnderTest,
			String testname, boolean testStatic, boolean testNonStatic,
			boolean testReflective, ExceptionExpectedOn... exceptionExpectedOn) {
		this.testClass = testClass;
		this.classUnderTest = classUnderTest;
		this.testname = testname;
		this.testStatic = testStatic;
		this.testNonStatic = testNonStatic;
		this.testReflective = testReflective;
		this.exceptionExpectedOn = EnumSet.noneOf(ExceptionExpectedOn.class);
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

	public boolean exceptionExpectedOnNonStaticAccess() {
		return this.getExceptionExpectedOn().contains(ExceptionExpectedOn.GET)
				|| this.getExceptionExpectedOn().contains(
						ExceptionExpectedOn.PUT);
	}

	public boolean exceptionExpectedOnStaticAccess() {
		return this.getExceptionExpectedOn().contains(
				ExceptionExpectedOn.GETSTATIC)
				|| this.getExceptionExpectedOn().contains(
						ExceptionExpectedOn.PUTSTATIC);
	}

}