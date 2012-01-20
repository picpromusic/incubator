package inc.util.serialization.token;

public class NewClass extends DefaultPositionMarkers implements ContentObject {

  private ClassDesc classDesc;

  public void setClassDesc(ClassDesc classdesc) {
    this.classDesc = classdesc;
  }

}
