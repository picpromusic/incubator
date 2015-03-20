package tbIncubator;

public class InteractionParameter {

	public final String name;
	public final String pk;
	public final Link dataTypeRef;

	public InteractionParameter(String name, String pk, Link dataTypeRef) {
		
		this.name = name;
		this.pk = pk;
		this.dataTypeRef = dataTypeRef;
	}

	public String getJavaName() {
		String name = TbElement.replaceAll(this.name);
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

}
