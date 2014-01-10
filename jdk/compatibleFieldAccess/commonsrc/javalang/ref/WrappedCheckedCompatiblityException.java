package javalang.ref;

public class WrappedCheckedCompatiblityException extends RuntimeException {

		/**
	 * 
	 */
	private static final long serialVersionUID = -7680078149303100645L;

		public WrappedCheckedCompatiblityException(Throwable checkedException) {
                if (checkedException instanceof RuntimeException) {
                        throw new InternalError(
                                        "Only non RuntimeException should be wrapped");
                }
                initCause(checkedException);
        }
}