package inc.util.serialization.token;

public interface MayContainReference<T extends ReferenceableParsedObject> {

    public abstract T getReference();

    public abstract boolean isReference();
    

}
