import java.lang.reflect.Field;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		System.out.println("Version 2012-05-23 05:40");
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<OLD>>>");
			TestOld.testIt();
		}
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<NEW>>>");
			TestNew.testIt();
		}
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<NEW2>>>");
			try {
				TestNew2.testIt();
				throw new RuntimeException("Exception expected");
			} catch (IllegalStateException e) {
				System.out.println("***" + e);
			}
		}
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<OLD_REFLECT>>>");
			OLD n = new OLD();
			Field field = n.getClass().getField("cause");
			System.out.println(field.get(n));
			RuntimeException newCause = new RuntimeException("OLD Reflective");
			field.set(n, newCause);
			System.out.println(field.get(n));
		}
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<NEW_REFLECT>>>");
			NEW n = new NEW();
			Field field = n.getClass().getField("cause");
			System.out.println(field.get(n));
			RuntimeException newCause = new RuntimeException("NEW Reflective");
			field.set(n, newCause);
			System.out.println(field.get(n));
		}
		for (int i = 0; i < TIMES; i++) {
			System.out.println("<<<NEW2_REFLECT>>>");
			NEW2 n = new NEW2();
			Field field = n.getClass().getField("cause");
			System.out.println(field.get(n));
			RuntimeException newCause = new RuntimeException("NEW2 Reflective");
			try {
				field.set(n, newCause);
				throw new RuntimeException("Exception expected");
			} catch (RuntimeException e) {
				System.out.println("***" + e);
			}
		}
	}
}
