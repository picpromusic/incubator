
public class NEW2 {

	private Throwable cause = new RuntimeException("INIT_NEW");
	
	@Accessor("cause")
	public Throwable getCause() {
		return cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		throw new IllegalStateException("Not allowed to change");
	}
	
}
