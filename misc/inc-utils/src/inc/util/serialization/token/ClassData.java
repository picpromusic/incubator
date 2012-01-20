package inc.util.serialization.token;

public class ClassData {
    private Object data;
    private FieldDesc fRef;
    private ClassDescInfo cRef;

    public Object getData() {
	return data;
    }

    public void setData(Object data) {
	this.data = data;
    }

    public FieldDesc getFieldRef() {
	return fRef;
    }

    public void setFieldRef(FieldDesc ref) {
	this.fRef = ref;
    }

    public ClassDescInfo getClassRef() {
	return cRef;
    }

    public void setClassRef(ClassDescInfo cRef) {
	this.cRef = cRef;
    }

    @Override
    public String toString() {
	return "ClassData: " + cRef.toString() + "::" + fRef.toString() + " := "
		+ data;
    }
}
