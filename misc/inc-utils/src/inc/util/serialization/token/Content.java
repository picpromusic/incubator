package inc.util.serialization.token;

import java.io.PrintStream;

public class Content extends DefaultPositionMarkers {

  private ContentObject object;

  public void setObject(ContentObject object) {
    this.object = object;
  }

  public void printContent(PrintStream out) {
    out.println(this);
  }

  @Override
  public String getParseObjectName() {
    return "Content";
  }

}
