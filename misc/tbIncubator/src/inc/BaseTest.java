package inc;

import java.util.Date;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;


public class BaseTest implements ZusammengesetzteInteraktionInterf {

	@Override
	public ITechnisch Technisch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILdapUndCoInteraktionen LdapUndCoInteraktionen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IKundeInteraktionen KundeInteraktionen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteraktionen Interaktionen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBestandInteraktionen BestandInteraktionen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBusinessDelegateInteraktionen BusinessDelegateInteraktionen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPmsSzenarien PmsSzenarien() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOberflaeche Oberflaeche() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String Wert(String string) {
		return Static.Wert(string);
	}

	@Override
	public String Name(String string) {
		return Static.Name(string);
	}

	@Override
	public String Text(String string) {
		return Static.Text(string);
	}

	@Override
	public String Schluessel(String string) {
		return Static.Schluessel(string);
	}

	protected Date Datum(String string) {
		return Static.Datum(string);
	}

	protected Integer Anzahl(String string) {
		return Static.Anzahl(string);
	}

}
