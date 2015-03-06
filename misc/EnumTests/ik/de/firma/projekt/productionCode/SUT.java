package de.firma.projekt.productionCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SUT {

	private List<WhatEver> seenElements;

	public SUT() {
		seenElements = new ArrayList<WhatEver>();
	}

	protected abstract OtherBackend getOtherBackendService();

	public void searchElements(String backendFilterString,
			Predicate<WhatEver> localfilter) {
		Stream<WhatEver> findAll = getOtherBackendService().findAll(
				backendFilterString);
		if (findAll != null) {
			Stream<WhatEver> filter = findAll.filter(localfilter);
			filter.forEach((e) -> {
				seenElements.add(e);
			});
		}
	}

	public List<WhatEver> getSeenElements() {
		return Collections.unmodifiableList(seenElements);
	}

	public static Predicate<WhatEver> all() {
		return new Predicate<WhatEver>() {

			@Override
			public boolean test(WhatEver t) {
				return true;
			}
		};
	}

	public void clearValues() {
		seenElements = new LinkedList<WhatEver>();
	}
}
