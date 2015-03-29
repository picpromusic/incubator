package tbIncubator.domain;

import java.util.ArrayList;
import java.util.List;

public class TestFall {

	private List<InteractionParameter> interactionParameter;

	public TestFall() {
		this.interactionParameter = new ArrayList<InteractionParameter>();
	}
	
	public void add(InteractionParameter next) {
		interactionParameter.add(next);
	}

}
