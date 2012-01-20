package inc.util.serialization.token;

public class ContentObjectReference<T extends DefaultPositionMarkers<T>> extends
	DefaultPositionMarkers<T> implements ContentObject{

    public ContentObjectReference(ReferenceableParsedObject ref) {
	setReference((T) ref);
    }

}
