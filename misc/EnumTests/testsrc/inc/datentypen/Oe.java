package inc.datentypen;

public enum Oe {
	Ik2IO(99761), //
	Sts3(11111),//
	Extern(00000), //
	;

	public final int nummer;

	Oe(int nummer) {
		this.nummer = nummer;
	}

}
