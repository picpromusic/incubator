public class BarBase {

	private int myPrivateField;

	public BarBase() {
		this.myPrivateField = 5;
	}

	@Override
	public String toString() {
		return "Bar:" + myPrivateField;
	}

}
