package inc.util.serialization.token;

import java.util.ArrayList;
import java.util.List;

public class NewArray extends DefaultPositionMarkers<NewArray> implements ContentObject{

    private ClassDesc classdesc;
    private ArrayList<ContentObject> list;

    public void setClassDesc(ClassDesc classdesc) {
	this.classdesc = classdesc;
	this.list = new ArrayList<>();
    }

    public void add(ContentObject object) {
	list.add(object);
    }
    
    public List<ContentObject> getContent() {
	return list;
    }

}
