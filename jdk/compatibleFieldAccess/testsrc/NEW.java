
public class NEW {

	private Throwable cause = new RuntimeException("INIT_NEW");
	
	@Accessor("cause")
	public Throwable getCause() {
		return cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		this.cause = cause;
	}
	
}
