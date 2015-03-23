package tbIncubator.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubCall {

	public final String interactionRef;
	public final List<Link> parameters;

	public SubCall(String interactionRef, ArrayList<Link> parameters) {
		this.interactionRef = interactionRef;
		this.parameters = Collections.unmodifiableList(parameters);
	}

}
