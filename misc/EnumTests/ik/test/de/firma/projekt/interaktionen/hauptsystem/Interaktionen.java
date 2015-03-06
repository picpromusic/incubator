package test.de.firma.projekt.interaktionen.hauptsystem;

import java.util.List;

import test.de.firma.project.datentypen.Mitarbeiter;
import test.de.firma.project.datentypen.Oe;
import test.de.firma.projekt.interaktionen.ldapUndCoMocks.MockOtherBackend;
import test.de.firma.projekt.technisch.datentypen.Bereich;
import junit.framework.Assert;
import de.firma.projekt.productionCode.OtherBackend;
import de.firma.projekt.productionCode.SUT;
import de.firma.projekt.productionCode.WhatEver;
import de.firma.testframework.AlleInteraktionen;
import de.firma.testframework.AlleInteraktionen.Destroyable;

public class Interaktionen {

	public static Interaktionen instance;
	private Oe oe;
	private SUT sut;
	private List<WhatEver> seenElements;

	public Interaktionen() {
		sut = new SUT() {

			@Override
			protected OtherBackend getOtherBackendService() {
				return MockOtherBackend.self().getInstance();
			}
		};
	}

	public static void setze_OE(Oe ik2io) {
		self().oe = ik2io;
	}

	public static void pruefe_Anzahl_Elemente_fuer_OE(int anzahl) {
		self().sut.clearValues();
		self().sut.searchElements(Integer.toString(self().oe.nummer), SUT.all());
		self().seenElements = self().sut.getSeenElements();
		Assert.assertEquals(anzahl, self().seenElements.size());
	}

	public static void pruefe_Mitarbeiter_an_Position(Bereich bereich,
			Mitarbeiter mitarbeiter) {
		List<WhatEver> subList = self().seenElements.subList(bereich.von()-1, bereich.bis());
		Assert.assertTrue(subList.contains(new WhatEver(mitarbeiter.mitarbeiterName)));
	}


	public static Interaktionen self() {
		return AlleInteraktionen.self(Interaktionen.class);
	}

	public static void pruefe_Anzahl_Elemente_fuer_Oee() {
		
	}

}
