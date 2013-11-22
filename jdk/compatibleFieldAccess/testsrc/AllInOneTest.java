import incubator.B;
import incubator.C;
import incubator.StcA;
import incubator.SubjectToChange;

import java.lang.reflect.Field;

public class AllInOneTest {
	private static final int TIMES = 1;

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		
		StcA sa = new StcA();
		System.out.println("Expected: " + "StcA:5");
		System.out.println("Reality : " + sa);

		B b = new B();
		System.out.println(b);
		System.out.println("Expected: " + "StcA:2	B:2 Super:2");
		System.out.println("Reality : " + b);
	}
}
