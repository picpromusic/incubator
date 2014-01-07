package incubator.dependency;

import incubator.StcA;

public class B extends StcA {
	
	public B() {
		// This "this" (instead of "super") is intended. Because Bytecode differs in both situations.
		// The class C contains the super test-case 
		this.field-=3;
	}

	@Override
	public String toString() {
		return super.toString() + "\tB:"+field+" Super:"+super.field;
	}
	
}
