package interaktionen.hauptsystem;

import inc.OtherBackend;
import inc.SUT;
import inc.WhatEver;
import inc.datentypen.Mitarbeiter;
import inc.datentypen.Oe;
import interaktionen.AlleInteraktionen;
import interaktionen.AlleInteraktionen.Destroyable;
import interaktionen.Bereich;
import interaktionen.ldapUndCoMocks.MockOtherBackend;

import java.util.List;

import junit.framework.Assert;

public class Interaktionen implements Destroyable {

	public static Interaktionen instance;
	private static ThreadLocal<Interaktionen> self = new ThreadLocal<Interaktionen>();
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
		if (self.get() == null) {
			self.set(new Interaktionen());
			AlleInteraktionen.register(self.get());
		}
		return self.get();
	}


	public void destroy() {
		if (self.get() == null) {
			throw new RuntimeException("Nothing to destroy");
		}
		self.set(null);
	}

}
