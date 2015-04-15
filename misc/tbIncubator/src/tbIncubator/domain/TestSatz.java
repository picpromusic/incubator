package tbIncubator.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tbIncubator.generator.HasSubCalls;

public class TestSatz extends TbElement implements HasParameters, HasSubCalls, HasRepresentatives{

	private final List<InteractionParameter> parameters;
	private final List<SubCall> subCalls;
	public final List<TestFall> testfaelle;
	private final List<Link> parameterValues;

	public TestSatz(String name, List<String> path, String pk,
			ArrayList<InteractionParameter> parameters,
			ArrayList<SubCall> subCalls, ArrayList<Link> parameterValues) {
		super(name, path, pk);
		this.parameterValues = Collections.unmodifiableList(parameterValues);
		this.parameters = Collections.unmodifiableList(parameters);
		this.subCalls = Collections.unmodifiableList(subCalls);
		List<TestFall> temp = new ArrayList<TestFall>();
		Iterator<InteractionParameter> iterator = parameters.iterator();
		while (iterator.hasNext()) {
			TestFall testfall = new TestFall();
			for (InteractionParameter para : parameters) {
				testfall.add(iterator.next());
			}
			temp.add(testfall);
		}
		this.testfaelle = Collections.unmodifiableList(temp);
	}

	@Override
	public Iterable<InteractionParameter> getParameters() {
		return parameters;
	}

	@Override
	public Iterable<SubCall> getSubCalls() {
		return subCalls;
	}
	
	public Iterable<Link> getParameterValues() {
		return parameterValues;
	}

	@Override
	public Iterable<Representative> getRepresentatives() {
		return new RepresentativeListBuildHelper(this).build();
	}

	public boolean parameterAnzahlPasst() {
		return parameters.size() == 0 || parameterValues.size() % parameters.size() == 0;
	}
	

}
