
public class NEW2 {

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	
	@Accessor("cause")
	public Throwable getCause() {
		return inner_cause;
	}
	
	@Accessor("cause")
	public void initCause(Throwable cause) {
		throw new IllegalStateException("Not allowed to change");
	}
	
}
