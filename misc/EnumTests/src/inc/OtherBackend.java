package inc;

import java.util.stream.Stream;

public interface OtherBackend {

	Stream<WhatEver> findAll(String backendFilterString);

}
