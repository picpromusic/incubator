package incubator;

import javalang.ref.Accessor;

public class StcA {

	private int field;
	
	public StcA() {
		this.field = 5;
	}

	@Override
	public String toString() {
		return "StcA:" + field;
	}

}
