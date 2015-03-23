package tbIncubator;

import java.util.Collections;
import java.util.List;

import tbIncubator.TbElementHandler.SubdivisionInfo;

public class Interaction extends TbElement {

	public final List<InteractionParameter> parameters;

	public Interaction(String name, List<String> path, String pk,
			List<InteractionParameter> parameters) {
		super(name, path, pk);
		this.parameters = Collections.unmodifiableList(parameters);
	}

}
