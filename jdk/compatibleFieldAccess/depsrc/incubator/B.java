package incubator;

public class B extends StcA {
	
	public B() {
		super.field-=3;
	}

	@Override
	public String toString() {
		return super.toString() + "\tB:"+field+" Super:"+super.field;
	}
	
}
