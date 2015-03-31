package inc;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;

public interface ZusammengesetzteInteraktionInterf {

	ITechnisch Technisch();

	ILdapUndCoInteraktionen LdapUndCoInteraktionen();

	IKundeInteraktionen KundeInteraktionen();

	IInteraktionen Interaktionen();

	IBestandInteraktionen BestandInteraktionen();

	IBusinessDelegateInteraktionen BusinessDelegateInteraktionen();

	IPmsSzenarien PmsSzenarien();

	IOberflaeche Oberflaeche();

}
