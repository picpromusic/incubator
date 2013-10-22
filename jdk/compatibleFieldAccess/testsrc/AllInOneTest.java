import java.lang.reflect.Field;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		SubjectToChange stc = new SubjectToChange();
		System.out.println(stc.cause);
		stc.cause = new RuntimeException("NEW");
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
	}
}
