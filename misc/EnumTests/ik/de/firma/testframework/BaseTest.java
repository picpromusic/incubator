package de.firma.testframework;

import org.junit.After;
import org.junit.AfterClass;


public class BaseTest {

	@After
	public void tearDown() {
		AlleInteraktionen.destroyAll();
	}

}
