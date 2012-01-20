package inc.util.serialization.token;

public class MagicToken extends ParsedToken {

    private ParsedWrapper<Short> magic;

    public MagicToken(ParsedWrapper<Short> magic) {
	this.magic = magic;
    }

    @Override
    public String toString() {
	return toString(Integer.toHexString(0xffff & magic.getContent()));
    }

}
