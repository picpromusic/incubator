package inc.util.serialization.token;

public class TypeToken extends ParsedToken {

    private final byte type;
    private String s;
    public final static byte TC_NULL = (byte) 0x70;
    public final static byte TC_REFERENCE = (byte) 0x71;
    public final static byte TC_CLASSDESC = (byte) 0x72;
    public final static byte TC_OBJECT = (byte) 0x73;
    public final static byte TC_STRING = (byte) 0x74;
    public final static byte TC_ARRAY = (byte) 0x75;
    public final static byte TC_CLASS = (byte) 0x76;
    public final static byte TC_BLOCKDATA = (byte) 0x77;
    public final static byte TC_ENDBLOCKDATA = (byte) 0x78;
    public final static byte TC_RESET = (byte) 0x79;
    public final static byte TC_BLOCKDATALONG = (byte) 0x7A;
    public final static byte TC_EXCEPTION = (byte) 0x7B;
    public final static byte TC_LONGSTRING = (byte) 0x7C;
    public final static byte TC_PROXYCLASSDESC = (byte) 0x7D;
    public final static byte TC_ENUM = (byte) 0x7E;

    public TypeToken(byte type) {
	this.type = type;
    }

    public byte getType() {
	return type;
    }

    @Override
    public String toString() {
	calcString();
	return toString(s);
    }

    private void calcString() {
	if (s == null) {
	    switch (type) {
	    case TC_NULL:
		s = "TC_NULL";
		break;
	    case TC_REFERENCE:
		s = "TC_REFERENCE";
		break;
	    case TC_CLASSDESC:
		s = "TC_CLASSDESC";
		break;
	    case TC_OBJECT:
		s = "TC_OBJECT";
		break;
	    case TC_STRING:
		s = "TC_STRING";
		break;
	    case TC_ARRAY:
		s = "TC_ARRAY";
		break;
	    case TC_CLASS:
		s = "TC_CLASS";
		break;
	    case TC_BLOCKDATA:
		s = "TC_BLOCKDATA";
		break;
	    case TC_ENDBLOCKDATA:
		s = "TC_ENDBLOCKDATA";
		break;
	    case TC_RESET:
		s = "TC_RESET";
		break;
	    case TC_BLOCKDATALONG:
		s = "TC_BLOCKDATALONG";
		break;
	    case TC_EXCEPTION:
		s = "TC_EXCEPTION";
		break;
	    case TC_LONGSTRING:
		s = "TC_LONGSTRING";
		break;
	    case TC_PROXYCLASSDESC:
		s = "TC_PROXYCLASSDESC";
		break;
	    case TC_ENUM:
		s = "TC_ENUM";
		break;
	    default:
		s = "UNKNOWN";
		break;
	    }
	}
    }

}
