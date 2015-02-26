package interaktionen.ldapUndCoMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.mockito.Mockito;

import inc.OtherBackend;
import inc.WhatEver;
import inc.datentypen.Mitarbeiter;
import interaktionen.AlleInteraktionen;
import interaktionen.AlleInteraktionen.Destroyable;

public class MockOtherBackend implements Destroyable{

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
		if (self.get() == null) {
			self.set(new MockOtherBackend());
			AlleInteraktionen.register(self.get());
		}
		return self.get();
	}

	public void destroy() {
		if (self() == null) {
			throw new RuntimeException("Nothing to destroy");
		}
		self.set(null);
	}

	
}
