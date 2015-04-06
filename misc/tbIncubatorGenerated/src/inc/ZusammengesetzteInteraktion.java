package inc;

 import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;

import java.math.BigDecimal;
import java.util.Date;

public class ZusammengesetzteInteraktion {

	protected static ITechnisch Technisch() {
		return null;
	}

	protected static ILdapUndCoInteraktionen LdapUndCoInteraktionen() {
		return null;
	}

	protected static IKundeInteraktionen KundeInteraktionen() {
		return null;
	}

	protected static IInteraktionen Interaktionen() {
		return null;
	}

	protected static IBestandInteraktionen BestandInteraktionen() {
		return null;
	}

	protected static IBusinessDelegateInteraktionen BusinessDelegateInteraktionen() {
		return null;
	}

	protected static IPmsSzenarien PmsSzenarien() {
		return null;
	}
	
	protected static IOberflaeche Oberflaeche() {
		return null;
	}

	protected static Date Datum(String string) {
		return Static.Datum(string);
	}
	
	protected static Integer Anzahl(String string) {
		return Static.Anzahl(string);
	}
	
	protected static BigDecimal Betrag(String string) {
		return Static.Betrag(string);
	}

}
