import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Retention(RUNTIME)
@Target(FIELD)
public @interface OldFieldAnnotationForTesting {
	String s();
	boolean b();
	Documented[] a();
}
