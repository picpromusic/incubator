package javalang.ref;

public class AsymeticAcessorError extends VerifyError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 431121324811321340L;

	public AsymeticAcessorError(Class<?> clazz, String name) {
		super("Asymetic Accessor for field " + name + " in " + clazz);
	}

}
