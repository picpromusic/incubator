package test.de.firma.project.testthemen.komponenteA;

import org.junit.Test;

import test.de.firma.project.datentypen.Oe;
import test.de.firma.project.vorgeschichte.LdapDefinitionen;
import test.de.firma.projekt.technisch.datentypen.Bereich;
import de.firma.testframework.BaseTest;
import static test.de.firma.project.datentypen.Mitarbeiter.*;
import static test.de.firma.projekt.interaktionen.hauptsystem.Interaktionen.*;

public class FirstTest extends BaseTest {

	@Test
	public void testDieserTestTraegtKeinenBesonderesKurzenNamen() {

		LdapDefinitionen.EinMitarbeiterInIoUndEinExterner();

		setze_OE(Oe.Sts3);
		pruefe_Anzahl_Elemente_fuer_OE(0);
		pruefe_Anzahl_Elemente_fuer_Oee();

		setze_OE(Oe.Ik2IO);
		pruefe_Anzahl_Elemente_fuer_OE(1);
		pruefe_Mitarbeiter_an_Position(Bereich.genau(1), SickelmannInIo);

		setze_OE(Oe.Extern);
		pruefe_Anzahl_Elemente_fuer_OE(1);
		pruefe_Mitarbeiter_an_Position(Bereich.genau(1), ExternerInExtern);

	}
}