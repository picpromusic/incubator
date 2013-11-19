package incubator;

public class C extends B{

	protected int field;
	
	public C() {
		this.field = 7;
		super.field+=10;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\tC:"+field+" Super:"+super.field;
	}
	
}
