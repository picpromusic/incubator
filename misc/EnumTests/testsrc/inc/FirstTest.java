package inc;

import inc.datentypen.Oe;
import interaktionen.Bereich;
import interaktionen.ldapUndCoMocks.LdapDefinitionen;

import org.junit.Test;

import static interaktionen.hauptsystem.Interaktionen.*;

import static inc.datentypen.Mitarbeiter.*;

public class FirstTest extends BaseTest {

	@Test
	public void test() {

		LdapDefinitionen.EinMitarbeiterInIoUndEinExterner();

		setze_OE(Oe.Sts3);
		pruefe_Anzahl_Elemente_fuer_OE(0);

		setze_OE(Oe.Ik2IO);
		pruefe_Anzahl_Elemente_fuer_OE(1);
		pruefe_Mitarbeiter_an_Position(Bereich.genau(1), SickelmannInIo);

		setze_OE(Oe.Extern);
		pruefe_Anzahl_Elemente_fuer_OE(1);
		pruefe_Mitarbeiter_an_Position(Bereich.genau(1), ExternerInExtern);

	}
}