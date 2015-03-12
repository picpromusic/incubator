package test.de.firma.project.datentypen;

public enum Mitarbeiter {
	
	// Werte
	SickelmannInIo("Sickelmann",Oe.Ik2IO), //
	ExternerInExtern("Ich bin Extern",Oe.Extern),//
	;
	
	
	// Struktur
	public final String mitarbeiterName;
	public final Oe oe;

	// Struktur übernehmen.
	Mitarbeiter(String name,Oe oe) {
		this.mitarbeiterName = name;
		this.oe = oe;
	}

	

}
