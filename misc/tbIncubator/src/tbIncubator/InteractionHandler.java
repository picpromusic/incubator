package tbIncubator;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InteractionHandler extends MyTbElementHandler<Interaction> {

	private String lastDataTypeRef;
	private ArrayList<InteractionParameter> parameters;

	public InteractionHandler(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> path) {
		super(endOfRedirect, path);
		this.parameters = new ArrayList<InteractionParameter>(6);
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("datatype-ref")) {
			lastDataTypeRef = attributes.getValue("pk");
		}

	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("parameter")) {
			parameters.add(new InteractionParameter(getInnerName(),
					getInnerPk(), new Link(lastDataTypeRef)));
		}
	}

	@Override
	public Interaction getTbElement() {
		ArrayList<String> path = getPathAsStringList();
		return new Interaction(getName(), path, getPk(), parameters);
	}

}
