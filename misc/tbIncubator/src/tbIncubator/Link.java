package tbIncubator;

public class Link {

	public final String ref;
	public final String name;

	public Link(String ref) {
		this(ref,null);
	}
	
	public Link(String ref, String name) {
		this.ref = ref;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return ref;
	}

}
