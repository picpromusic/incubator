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
		private List<ExceptionExpectedOn> runtimeExceptionExpectedOn;
		private List<ExceptionExpectedOn> checkedExceptionExpectedOn;

		public DescriptionOfTestSentence(String name) {
			this.testname = name;
			runtimeExceptionExpectedOn = new ArrayList<>(2);
			checkedExceptionExpectedOn = new ArrayList<>(2);

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

		public DescriptionOfTestSentence runtimeExceptionOn(
				ExceptionExpectedOn ex) {
			this.runtimeExceptionExpectedOn.add(ex);
			return this;
		}

		public DescriptionOfTestSentence checkedExceptionOn(
				ExceptionExpectedOn ex) {
			this.checkedExceptionExpectedOn.add(ex);
			return this;
		}

		public void testIt(List<DescriptionOfTest> tests) {
			ExceptionExpectedOn[] runtime = runtimeExceptionExpectedOn
					.toArray(new ExceptionExpectedOn[runtimeExceptionExpectedOn
							.size()]);
			ExceptionExpectedOn[] checked = checkedExceptionExpectedOn
					.toArray(new ExceptionExpectedOn[checkedExceptionExpectedOn
							.size()]);
			tests.add(new DescriptionOfTest(forClass, testingClass, testname,
					testStatic, testNonStatic, testReflective, runtime, checked));
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
	private final EnumSet<ExceptionExpectedOn> runtimeExceptionExpectedOn;
	private final EnumSet<ExceptionExpectedOn> checkedExceptionExpectedOn;
	private final boolean testNonStatic;

	public DescriptionOfTest(Class<?> testClass, Class<?> classUnderTest,
			String testname, boolean testStatic, boolean testNonStatic,
			boolean testReflective,
			ExceptionExpectedOn[] runtimeExceptionExpectedOn,
			ExceptionExpectedOn[] checkedExceptionExpectedOn) {
		this.testClass = testClass;
		this.classUnderTest = classUnderTest;
		this.testname = testname;
		this.testStatic = testStatic;
		this.testNonStatic = testNonStatic;
		this.testReflective = testReflective;
		this.runtimeExceptionExpectedOn = EnumSet
				.noneOf(ExceptionExpectedOn.class);
		for (ExceptionExpectedOn ex : runtimeExceptionExpectedOn) {
			this.runtimeExceptionExpectedOn.add(ex);
		}
		this.checkedExceptionExpectedOn = EnumSet
				.noneOf(ExceptionExpectedOn.class);
		for (ExceptionExpectedOn ex : checkedExceptionExpectedOn) {
			this.runtimeExceptionExpectedOn.add(ex);
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

	public EnumSet<ExceptionExpectedOn> getRuntimeExceptionExpectedOn() {
		return runtimeExceptionExpectedOn;
	}

	public EnumSet<ExceptionExpectedOn> getCheckedExceptionExpectedOn() {
		return this.checkedExceptionExpectedOn;
	}

	public boolean isTestNonStatic() {
		return testNonStatic;
	}

	public boolean exceptionExpectedOnNonStaticAccess() {
		return this.getRuntimeExceptionExpectedOn().contains(
				ExceptionExpectedOn.GET)
				|| this.getRuntimeExceptionExpectedOn().contains(
						ExceptionExpectedOn.PUT);
	}

	public boolean exceptionExpectedOnStaticAccess() {
		return this.getRuntimeExceptionExpectedOn().contains(
				ExceptionExpectedOn.GETSTATIC)
				|| this.getRuntimeExceptionExpectedOn().contains(
						ExceptionExpectedOn.PUTSTATIC);
	}

	public static DescriptionOfTestSentence createNew(String name) {
		return new DescriptionOfTestSentence(name);
	}

}