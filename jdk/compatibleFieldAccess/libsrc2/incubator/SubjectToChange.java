package incubator;
import java.lang.reflect.Accessor;

public class SubjectToChange {

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	private static Object inner_staticField = "ORIG_VALUE_NEW";

	@Accessor("cause")
	public Throwable getCause() {
		return inner_cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		throw new IllegalStateException("Not allowed to change the cause. This message is intendend");
	}
	
	@Accessor("staticField")
	public static Object getFoo() {
		return inner_staticField;
	}

	@Accessor("staticField")
	public static void setFoo(Object value) {
		throw new IllegalStateException("PutStatic of staticField not allowed anymore. This message is intendend");
	}
}
