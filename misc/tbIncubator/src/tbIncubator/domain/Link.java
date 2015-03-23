package tbIncubator.domain;

public class Link {

	public enum LinkType {
		REPRESENTATIVE, PARAMETER
	}

	public final String ref;
	public final String name;
	public final LinkType type;

	public Link(String ref, LinkType type) {
		this(ref,null,type);
	}
	
	public Link(String ref, String name, LinkType type) {
		this.ref = ref;
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ref;
	}

}
