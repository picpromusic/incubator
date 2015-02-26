package interaktionen.ldapUndCoMocks;

import static interaktionen.ldapUndCoMocks.MockOtherBackend.*;

import static inc.datentypen.Mitarbeiter.*;

public class LdapDefinitionen {

	public static void EinMitarbeiterInIoUndEinExterner() {
		stelleSicherMitarbeiter(SickelmannInIo);
		stelleSicherMitarbeiter(ExternerInExtern);
	}

}
