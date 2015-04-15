package inc;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;
import inc.tf.InteractionManager;

import java.math.BigDecimal;
import java.util.Date;

public class ZusammengesetzteInteraktion {

	protected static ITechnisch Technisch() {
		return InteractionManager.get(ITechnisch.class);
	}

	protected static ILdapUndCoInteraktionen LdapUndCoInteraktionen() {
		return InteractionManager.get(ILdapUndCoInteraktionen.class);
	}

	protected static IKundeInteraktionen KundeInteraktionen() {
		return InteractionManager.get(IKundeInteraktionen.class);
	}

	protected static IInteraktionen Interaktionen() {
		return InteractionManager.get(IInteraktionen.class);
	}

	protected static IBestandInteraktionen BestandInteraktionen() {
		return InteractionManager.get(IBestandInteraktionen.class);
	}

	protected static IBusinessDelegateInteraktionen BusinessDelegateInteraktionen() {
		return InteractionManager.get(IBusinessDelegateInteraktionen.class);
	}

	protected static IPmsSzenarien PmsSzenarien() {
		return InteractionManager.get(IPmsSzenarien.class);
	}

	protected static IOberflaeche Oberflaeche() {
		return InteractionManager.get(IOberflaeche.class);
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
