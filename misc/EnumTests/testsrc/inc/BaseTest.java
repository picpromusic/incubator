package inc;

import interaktionen.AlleInteraktionen;

public class BaseTest {

	public void tearDown() {
		AlleInteraktionen.destroyAll();
	}

}
