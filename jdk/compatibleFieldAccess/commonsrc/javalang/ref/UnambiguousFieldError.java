package javalang.ref;

public class UnambiguousFieldError extends LinkageError{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4635001196691171135L;

	public UnambiguousFieldError() {
		super();
	}
	
	public UnambiguousFieldError(String msg) {
		super(msg);
	}
	
	public UnambiguousFieldError(String msg, Throwable cause) {
		super(msg,cause);
	}
}
