import incubator.B;
import incubator.C;
import incubator.StcA;
import incubator.SubjectToChange;

import java.lang.reflect.Field;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		
		SubjectToChange stc = new SubjectToChange();
		System.out.println(stc.cause);
		try {
			stc.cause = new RuntimeException("NEW");
		} catch (IllegalStateException ex) {
			System.out.println("Exception occured " + ex.getMessage());
		}
		System.out.println(stc.cause);

		boolean testReflect = Boolean.getBoolean("test-reflection");
		if (testReflect) {
			for (int i = 0; i < TIMES; i++) {
				SubjectToChange n = new SubjectToChange();
				Field field = n.getClass().getField("cause");
				System.out.println(field.get(n));
				RuntimeException newCause = new RuntimeException("Reflective");
				field.set(n, newCause);
				System.out.println(field.get(n));
			}
		}

		System.out.println(SubjectToChange.staticField);
		try {
			SubjectToChange.staticField = "NEW STATIC VALUE";
		} catch (IllegalStateException ex) {
			System.out.println("Exception occured " + ex.getMessage());
		}
		System.out.println(SubjectToChange.staticField);

		if (testReflect) {
			for (int i = 0; i < TIMES; i++) {
				Field field = SubjectToChange.class.getField("staticField");
				System.out.println(field.get(null));
				field.set(null, "NEW STATIC REFLECTIVE VALUE");
				System.out.println(field.get(null));
			}
		}

		StcA sa = new StcA();
		System.out.println("Expected: " + "StcA:5");
		System.out.println("Reality : " + sa);

		B b = new B();
		System.out.println("Expected: " + "StcA:2	B:2 Super:2");
		System.out.println("Reality : " + b);

		try {
			C c = new C();
			System.out.println("Expected: "
					+ "StcA:12	B:12 Super:12	C:7 Super:12");
			System.out.println("Reality : " + c);
		} catch (IllegalStateException ex) {
			System.out.println("Exception occured " + ex.getMessage());
		}

	}
}
