package inc.util.serialization.token;

public class NewHandleToken extends ParsedToken {

    private final int handle;

    public NewHandleToken(int i) {
	this.handle = i;
    }

    public int getHandle() {
        return handle;
    }
    
    @Override
    public String toString() {
        return toString(Integer.toHexString(handle));
    }

}
