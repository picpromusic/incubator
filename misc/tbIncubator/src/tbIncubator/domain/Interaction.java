package tbIncubator.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tbIncubator.generator.HasSubCalls;

public class Interaction extends TbElement implements HasParameters,HasSubCalls{

	public final String description;
	private final List<InteractionParameter> parameters;
	private final List<SubCall> subCalls;

	public Interaction(String name, List<String> path, String pk, String description,
			List<InteractionParameter> parameters, ArrayList<SubCall> subCalls) {
		super(name, path, pk);
		this.description = description;
		this.parameters = Collections.unmodifiableList(parameters);
		this.subCalls = Collections.unmodifiableList(subCalls);
	}
	
	
	@Override
	public Iterable<InteractionParameter> getParameters() {
		return parameters;
	}


	@Override
	public Iterable<SubCall> getSubCalls() {
		return subCalls;
	}

}
