package inc.util.serialization.token;

public class NewString extends DefaultPositionMarkers implements ContentObject{

    private UTF8 utf8;

    public void setString(UTF8 utf8) {
	this.utf8 = utf8;
    }

}
