package tbIncubator;

import java.util.Collections;
import java.util.List;

public class Representative {

	public final String name;
	public final String pk;
	public final List<Link> representativeLinks;
	private TbElement definedIn;

	public Representative(String name, String pk, List<Link> representativeLinks) {
		this.name = name;
		this.pk = pk;
		this.representativeLinks = Collections.unmodifiableList(representativeLinks);
	}
	
	public void setDefinedIn(TbElement definedIn) {
		this.definedIn = definedIn;
	}
	
	public TbElement getDefinedIn() {
		return definedIn;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Object toJavaName() {
		return DataType.replaceAll(name.trim().replace('.', '_').replace('/', '_'));
	}

}
