import incubator.SimpleSubjectToChange;

public class SimpleTest {

	public static void main(String[] args) throws ReflectiveOperationException,
			SecurityException, IllegalArgumentException {
		System.out.println("Starting SimpleTest");

		{
			SimpleSubjectToChange s = new SimpleSubjectToChange();

			s.weight = 0.1f;
			s.x = 6;
			s.y = 8;

			test(s.x * s.weight, 0.6f, 0.1f);
			test(s.y * s.weight, 0.8f, 0.1f);
			test((float) (Math.sqrt(s.x * s.x + s.y * s.y) * s.weight), 1.0f,
					0.1f);
		}

		{
			SimpleSubjectToChange s = new SimpleSubjectToChange();

			s.x = 6;
			s.y = 8;
			s.weight = 0.1f;

			test(s.x * s.weight, 0.6f, 0.1f);
			test(s.y * s.weight, 0.8f, 0.1f);
			test((float) (Math.sqrt(s.x * s.x + s.y * s.y) * s.weight), 1.0f,
					0.1f);
		}

		System.out.println("End SimpleTest");

	}

	private static void test(float f, float exp, float r) {
		if (Math.abs(f - exp) > r) {
			System.out.println("Unexpected Value: " + f + " Expected is " + exp
					+ " in a range of " + r);
		}
	}
}
