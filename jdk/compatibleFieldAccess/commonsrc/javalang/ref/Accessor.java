package javalang.ref;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Accessor {

	/**
	 * The name of the field that for that the annotated Method is emulating the access to.
	 * If the name is not specified, the runtime tries to determine the name based on the methodname 
	 * @return the name of the field.
	 */
	String value() default "";
	boolean allowNonStaticAccess() default false;

}
