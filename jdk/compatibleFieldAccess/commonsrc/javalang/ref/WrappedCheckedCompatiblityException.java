package javalang.ref;

public class WrappedCheckedCompatiblityException extends RuntimeException {

        public WrappedCheckedCompatiblityException(Throwable checkedException) {
                if (checkedException instanceof RuntimeException) {
                        throw new InternalError(
                                        "Only non RuntimeException should be wrapped");
                }
                initCause(checkedException);
        }
}