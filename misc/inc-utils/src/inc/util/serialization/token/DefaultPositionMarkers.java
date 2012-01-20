package inc.util.serialization.token;

public class DefaultPositionMarkers<T extends DefaultPositionMarkers<T>>
	implements HasPositionMarkers, ReferenceableParsedObject,
	MayContainReference<T> {

    private long start = -1;
    private long end = -1;
    private T reference;

    public DefaultPositionMarkers() {
	this.reference = (T) this;
    }

    @Override
    public boolean isReference() {
	return reference != this;
    }

    @Override
    public T getReference() {
	return reference != this ? reference : null;
    }

    public void setReference(T ref) {
	this.reference = ref;
    }

    @Override
    public void setStartPos(long pos) {
	start = pos;
    }

    @Override
    public void setEndPos(long pos) {
	end = pos;
    }

    @Override
    public long getStartPos() {
	return start;
    }

    @Override
    public long getEndPos() {
	return end;
    }

    public String getParseObjectName() {
	return this.getClass().getName();
    }

    @Override
    public String toString() {
	return toString(getParseObjectName(), this.getStartPos(),
		this.getEndPos());
    }

    public static String toString(String objectName, long start, long end) {
	return objectName + ": " + (end - start) + " @(0x"
		+ Long.toHexString(start) + "-0x" + Long.toHexString(end) + ")";
    }

}
