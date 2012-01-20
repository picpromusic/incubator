package inc.util.serialization.token;

public class FieldDesc extends DefaultPositionMarkers{

  private char type;
  private UTF8 fieldName;
  private ContentObject className;

  public ContentObject getClassName() {
    return className;
  }

  public UTF8 getFieldName() {
    return fieldName;
  }

  public char getType() {
    return type;
  }

  public void setType(char type) {
    this.type = type;
  }

  public void setFieldName(UTF8 utf8) {
    this.fieldName = utf8;
  }

  public void setClassName(ContentObject contentObject) {
    this.className = contentObject;
  }

  @Override
    public String toString() {
	return super.toString() + ":" + this.type + " " + this.fieldName.getContent();
    }
}
