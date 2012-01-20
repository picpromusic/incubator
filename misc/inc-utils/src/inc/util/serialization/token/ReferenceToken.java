package inc.util.serialization.token;

public class ReferenceToken extends ParsedToken {

    private final int handle;

    public ReferenceToken(int handle) {
	this.handle = handle;
	// TODO Auto-generated constructor stub
    }

    public int getHandle() {
        return handle;
    }
    
    @Override
    public String toString() {
        return toString(Integer.toHexString(handle));
    }

}
