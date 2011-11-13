
public class NEW {

	private Throwable cause = new RuntimeException("INIT_NEW");
	
	@AccessorMethod("cause")
	public Throwable getCause() {
		return cause;
	}
	
	@AccessorMethod("cause")
	public void initCause(Throwable cause) {
		this.cause = cause;
	}
	
}
