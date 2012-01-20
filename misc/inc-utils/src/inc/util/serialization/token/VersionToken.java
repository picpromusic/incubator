package inc.util.serialization.token;

public class VersionToken extends ParsedToken {

    private final ParsedWrapper<Short> version;

    public VersionToken(ParsedWrapper<Short> version) {
	this.version = version;
    }
    
    @Override
    public String toString() {
        return toString(version.getContent());
    }

}
