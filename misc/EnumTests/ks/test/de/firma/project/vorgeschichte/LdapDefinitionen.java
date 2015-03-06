package test.de.firma.project.vorgeschichte;


import static test.de.firma.project.datentypen.Mitarbeiter.*;
import static test.de.firma.projekt.interaktionen.ldapUndCoMocks.MockOtherBackend.*;

public class LdapDefinitionen {

	public static void EinMitarbeiterInIoUndEinExterner() {
		stelleSicherMitarbeiter(SickelmannInIo);
		stelleSicherMitarbeiter(ExternerInExtern);
	}

}
