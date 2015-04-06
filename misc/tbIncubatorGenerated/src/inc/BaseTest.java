package inc;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;

import java.math.BigDecimal;
import java.util.Date;


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
	public Date Datum(String string) {
		return Static.Datum(string);
	}

	@Override
	public Integer Anzahl(String string) {
		return Static.Anzahl(string);
	}
	
	@Override
	public BigDecimal Betrag(String string) {
		return Static.Betrag(string);
	}

	@Override
	public BigDecimal Prozent(String string) {
		return Static.Prozent(string);
	}

}
