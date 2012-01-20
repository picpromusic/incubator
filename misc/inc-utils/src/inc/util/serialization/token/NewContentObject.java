package inc.util.serialization.token;

import java.util.List;

public class NewContentObject extends DefaultPositionMarkers implements ContentObject {

  private ClassDesc classDesc;
  private List<ClassData> classData;

  public void setClassDesc(ClassDesc classdesc) {
    this.classDesc = classdesc;
  }

  public void setClassData(List<ClassData> list) {
    this.classData = list;
  }

  public ClassDesc getClassDesc() {
    return this.classDesc;
  }

}
