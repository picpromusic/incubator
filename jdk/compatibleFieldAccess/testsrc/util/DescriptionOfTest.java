package util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DescriptionOfTest {

	public static class DescriptionOfTestSentence {

		private Class testingClass;
		private Class forClass;
		private boolean testStatic;
		private boolean testNonStatic;
		private boolean testReflective;
		private final String testname;
		private List<ExceptionExpectedOn> exceptionExpectedOn;

		public DescriptionOfTestSentence(String name) {
			this.testname = name;
			exceptionExpectedOn = new ArrayList<>(2);

		}

		public DescriptionOfTestSentence forClass(Class forClass) {
			checkSet(this.forClass, forClass, "forClass");
			this.forClass = forClass;
			return this;
		}

		public DescriptionOfTestSentence testing(Class testingClass) {
			checkSet(this.testingClass, testingClass, "testingClass");
			this.testingClass = testingClass;
			return this;
		}

		public DescriptionOfTestSentence testStatic() {
			this.testStatic = true;
			return this;
		}

		public DescriptionOfTestSentence testNonStatic() {
			this.testNonStatic = true;
			return this;
		}

		public DescriptionOfTestSentence testReflective() {
			this.testReflective = true;
			return this;
		}

		public DescriptionOfTestSentence exceptionOn(ExceptionExpectedOn ex) {
			this.exceptionExpectedOn.add(ex);
			return this;
		}

		public void testIt(List<DescriptionOfTest> tests) {
			ExceptionExpectedOn[] exceptions = exceptionExpectedOn
					.toArray(new ExceptionExpectedOn[exceptionExpectedOn.size()]);
			tests.add(new DescriptionOfTest(forClass, testingClass, testname,
					testStatic, testNonStatic, testReflective, exceptions));
		}

		private void checkSet(Object oldValue, Object newValue, String name) {
			if (newValue == null) {
				throw new IllegalArgumentException(name + " must not be null");
			}
			if (oldValue != null) {
				throw new IllegalStateException(name + " already set");
			}
		}

	}

	private final Class<?> testClass;
	private final Class<?> classUnderTest;
	private final String testname;
	private final boolean testStatic;
	private final boolean testReflective;
	private final EnumSet<ExceptionExpectedOn> exceptionExpectedOn;
	private final boolean testNonStatic;

	public DescriptionOfTest(Class<?> testClass, Class<?> classUnderTest,
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

	public static DescriptionOfTestSentence createNew(String name) {
		return new DescriptionOfTestSentence(name);
	}
}