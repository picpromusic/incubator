package test.de.firma.project.datentypen;

public enum Mitarbeiter {
	SickelmannInIo("Sickelmann",Oe.Ik2IO), //
	ExternerInExtern("Ich bin Extern",Oe.Extern),//
	;
	
	public final String mitarbeiterName;
	public final Oe oe;

	Mitarbeiter(String name,Oe oe) {
		this.mitarbeiterName = name;
		this.oe = oe;
	}

	

}
