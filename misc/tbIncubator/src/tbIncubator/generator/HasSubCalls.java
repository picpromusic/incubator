package tbIncubator.generator;

import tbIncubator.domain.SubCall;

public interface HasSubCalls {

	Iterable<SubCall> getSubCalls();

}
