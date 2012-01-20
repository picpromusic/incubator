package inc.util.serialization.token;

public class ParsedToken {
    @Override
    public String toString() {
        return this.getClass().getName();
    }

    protected String toString(Object object) {
        return this.getClass().getName() + " : " + object;
    }
}
