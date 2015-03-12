package test.de.firma.projekt.interaktionen.ldapUndCoMocks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;

import test.de.firma.project.datentypen.Mitarbeiter;
import de.firma.projekt.productionCode.OtherBackend;
import de.firma.projekt.productionCode.WhatEver;
import de.firma.testframework.AlleInteraktionen;

public class MockOtherBackend{

	private OtherBackend mock;
	
	private Map<Integer,List<WhatEver>> whatEvers = new HashMap<Integer, List<WhatEver>>();
	
	private static ThreadLocal<MockOtherBackend> self = new ThreadLocal<MockOtherBackend>();

	public OtherBackend getInstance() {
		if (mock == null) {
			mock = Mockito.mock(OtherBackend.class);
		}
		return mock;
	}

	public static void stelleSicherMitarbeiter(Mitarbeiter mitarbeiter) {
		List<WhatEver> list = self().whatEvers.get(mitarbeiter.oe.nummer);
		if (list == null) {
			list = new LinkedList<WhatEver>();
		}
		list.add(new WhatEver(mitarbeiter.mitarbeiterName));
		Mockito.when(self().getInstance().findAll(Integer.toString(mitarbeiter.oe.nummer))).thenReturn(list.stream());
	}


	public static MockOtherBackend self() {
		return AlleInteraktionen.self(MockOtherBackend.class);
	}
	
}
