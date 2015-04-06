package inc;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;

import java.math.BigDecimal;
import java.util.Date;

public interface ZusammengesetzteInteraktionInterf {

	ITechnisch Technisch();

	ILdapUndCoInteraktionen LdapUndCoInteraktionen();

	IKundeInteraktionen KundeInteraktionen();

	IInteraktionen Interaktionen();

	IBestandInteraktionen BestandInteraktionen();

	IBusinessDelegateInteraktionen BusinessDelegateInteraktionen();

	IPmsSzenarien PmsSzenarien();

	IOberflaeche Oberflaeche();

	Date Datum(String string);

	Integer Anzahl(String string);

	BigDecimal Betrag(String string);

	BigDecimal Prozent(String string);
}
