package tbIncubator.saxHandlers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Link.LinkType;
import tbIncubator.domain.SubCall;

public class InteractionHandler extends TbElementHandler<Interaction> {

	private String lastDataTypeRef;
	private ArrayList<InteractionParameter> parameters;
	private ArrayList<SubCall> subCalls;
	private ArrayList<Link> subCallParameters;
	private String lastInteractionRef;

	public InteractionHandler(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> path) {
		super(endOfRedirect, path);
		this.parameters = new ArrayList<InteractionParameter>(6);
		this.subCalls = new ArrayList<SubCall>();
		this.subCallParameters = new ArrayList<Link>();
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("datatype-ref")) {
			lastDataTypeRef = attributes.getValue("pk");
		} else if (localName.equals("interaction-ref")) {
			lastInteractionRef = attributes.getValue("pk");
		} else if (localName.equals("representative-ref")) {
			subCallParameters.add(new Link(attributes.getValue("pk"),
					LinkType.REPRESENTATIVE));
		} else if (localName.equals("parent-parameter-ref")) {
			subCallParameters.add(new Link(attributes.getValue("pk"),
					LinkType.PARAMETER));
		}

	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("parameter")) {
			parameters.add(new InteractionParameter(getInnerName(),
					getInnerPk(), new Link(lastDataTypeRef,
							LinkType.REPRESENTATIVE)));
		} else if (localName.equals("interaction-call")) {
			subCalls.add(new SubCall(lastInteractionRef, new ArrayList<Link>(
					subCallParameters)));
			subCallParameters.clear();
		}
	}

	@Override
	public Interaction getTbElement() {
		ArrayList<String> path = getPathAsStringList();
		return new Interaction(getName(), path, getPk(),
				getInnerStringValue("description"), parameters,subCalls);
	}

}
