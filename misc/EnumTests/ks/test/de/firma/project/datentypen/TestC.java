package test.de.firma.project.datentypen;

public enum TestC {
// ###########WERTE#ANFANG###############
	Wert1(1,Oe.Extern),//
// ###########WERTE#ENDE#################

// Das Semikolon muss stehen bleiben.
	;

// ###########STRUKTUR#ANFANG############
	/**
	 * 
	 */
	public final int nummer;
	
	/**
	 * 
	 */
	public final Oe oe;
// ###########STRUKTUR#ENDE##############


// ###########TECHNISCH#ANFANG###########
	TestC(int nummer,Oe oe) {
		this.nummer = nummer;
		this.oe = oe;
	}
// ###########TECHNISCH#ENDE###########}
}