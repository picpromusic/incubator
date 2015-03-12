package test.de.firma.project.datentypen;

public enum Oe {

	// Werte
	Ik2IO(99761), //
	Sts3(11111),//
	Extern(00000), //
	;

	// Struktur
	public final int nummer;

	
	// Struktur übernehmen
	Oe(int nummer) {
		this.nummer = nummer;
	}

}
