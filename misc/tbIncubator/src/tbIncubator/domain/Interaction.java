package tbIncubator.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tbIncubator.TbElementHandler.SubdivisionInfo;

public class Interaction extends TbElement {

	public final String description;
	public final List<InteractionParameter> parameters;
	public final List<SubCall> subCalls;

	public Interaction(String name, List<String> path, String pk, String description,
			List<InteractionParameter> parameters, ArrayList<SubCall> subCalls) {
		super(name, path, pk);
		this.description = description;
		this.parameters = Collections.unmodifiableList(parameters);
		this.subCalls = Collections.unmodifiableList(subCalls);
	}

}
