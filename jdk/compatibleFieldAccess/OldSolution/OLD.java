import java.lang.annotation.Documented;

public class OLD {

	@OldFieldAnnotationForTesting(a = { @Documented, @Documented }, b = true, s = "DEMO_NON-STATIC-FIELD for <<<OLD>>>")
	public Throwable cause = new RuntimeException("INIT_OLD");

	@OldFieldAnnotationForTesting(a = { @Documented }, b = false, s = "DEMO_STATIC-FIELD for <<<OLD>>>")
	public static Object staticField = "ORIG_VALUE_OLD";

}
