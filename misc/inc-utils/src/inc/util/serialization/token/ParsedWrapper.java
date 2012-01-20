package inc.util.serialization.token;

public class ParsedWrapper<T> extends DefaultPositionMarkers{

  private T content;

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
    
  }

  @Override
  public String toString() {
    return super.toString() + " --> " + content;
  }
}
