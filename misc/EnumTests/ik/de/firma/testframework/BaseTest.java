package de.firma.testframework;


public class BaseTest {

	public void tearDown() {
		AlleInteraktionen.destroyAll();
	}

}
