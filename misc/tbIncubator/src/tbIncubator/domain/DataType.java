package tbIncubator.domain;

import java.util.Collections;
import java.util.List;

public class DataType extends TbElement {

	public final List<Representative> representatives;
	public final List<Link> fieldLinks;

	public DataType(String name, List<String> path, String pk,
			List<Representative> representatives, List<Link> fieldLinks) {
		super(name, path, pk);
		this.representatives = Collections.unmodifiableList(representatives);
		this.fieldLinks = Collections.unmodifiableList(fieldLinks);
	}

	@Override
	public String toString() {
		return name + " " + representatives;
	}

}
