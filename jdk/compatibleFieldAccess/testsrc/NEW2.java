
public class NEW2 {

	private Throwable inner_cause = new RuntimeException("INIT_NEW2");
	
	@AccessorMethod("cause")
	public Throwable getCause() {
		return inner_cause;
	}
	
	@AccessorMethod("cause")
	public void initCause(Throwable cause) {
		throw new IllegalStateException("Not allowed to change");
	}
	
}
