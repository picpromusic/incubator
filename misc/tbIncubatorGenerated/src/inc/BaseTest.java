package inc;

import inc.Umsysteme.IBestandInteraktionen;
import inc.Umsysteme.IBusinessDelegateInteraktionen;
import inc.Umsysteme.IKundeInteraktionen;
import inc.Umsysteme.ILdapUndCoInteraktionen;
import inc._Szenarien.IPmsSzenarien;
import inc.allgemein.IInteraktionen;
import inc.impl.BestandImpl;
import inc.impl.BusinessDelegateImpl;
import inc.impl.InteraktionenImpl;
import inc.impl.KundeInteraktionenImpl;
import inc.impl.LdapUndCoImpl;
import inc.impl.TechnischImpl;
import inc.tf.ruleImpl.CleanupImplementations;
import inc.tf.ruleImpl.SetImplementation;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class BaseTest implements ZusammengesetzteInteraktionInterf {

	@Rule
	public TestRule setImpl = RuleChain.outerRule(new CleanupImplementations())//
			.around(new SetImplementation(TechnischImpl.class))//
			.around(new SetImplementation(LdapUndCoImpl.class))//
			.around(new SetImplementation(KundeInteraktionenImpl.class))//
			.around(new SetImplementation(InteraktionenImpl.class))//
			.around(new SetImplementation(BestandImpl.class))//
			.around(new SetImplementation(BusinessDelegateImpl.class))//
			.around(new SetImplementation(PmsSzenarienImpl.class))//
	;

	@Override
	public ITechnisch Technisch() {
		return ZusammengesetzteInteraktion.Technisch();
	}

	@Override
	public ILdapUndCoInteraktionen LdapUndCoInteraktionen() {
		return ZusammengesetzteInteraktion.LdapUndCoInteraktionen();
	}

	@Override
	public IKundeInteraktionen KundeInteraktionen() {
		return ZusammengesetzteInteraktion.KundeInteraktionen();
	}

	@Override
	public IInteraktionen Interaktionen() {
		return ZusammengesetzteInteraktion.Interaktionen();
	}

	@Override
	public IBestandInteraktionen BestandInteraktionen() {
		return ZusammengesetzteInteraktion.BestandInteraktionen();
	}

	@Override
	public IBusinessDelegateInteraktionen BusinessDelegateInteraktionen() {
		return ZusammengesetzteInteraktion.BusinessDelegateInteraktionen();
	}

	@Override
	public IPmsSzenarien PmsSzenarien() {
		return ZusammengesetzteInteraktion.PmsSzenarien();
	}

	@Override
	public IOberflaeche Oberflaeche() {
		return ZusammengesetzteInteraktion.Oberflaeche();
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
