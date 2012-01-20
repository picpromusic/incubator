package inc.util.serialization.token;

public class ClassDesc extends DefaultPositionMarkers<ClassDesc> {

    private UTF8 className;
    private ParsedWrapper<Long> serialVersionUid;
    private ClassDescInfo cdi;

    public UTF8 getClassName() {
	return className;
    }

    public ParsedWrapper<Long> getSerialVersionUid() {
	return serialVersionUid;
    }

    public ClassDescInfo getClassDescInfo() {
	return cdi;
    }

    public void setClassName(UTF8 string) {
	this.className = string;
    }

    public void setSerialVersionUid(ParsedWrapper<Long> parsedWrapper) {
	this.serialVersionUid = parsedWrapper;

    }

    public void setClassDescInfo(ClassDescInfo cdi) {
	this.cdi = cdi;

    }

    @Override
    public String toString() {
	return super.toString() + " -> " + getClassNameAsString();
    }

    private String getClassNameAsString() {
	return className != null ? className.getContent() : "<NOT KNOWN>";
    }
}
