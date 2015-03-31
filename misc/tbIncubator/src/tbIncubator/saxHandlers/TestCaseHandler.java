package tbIncubator.saxHandlers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Link.LinkType;
import tbIncubator.domain.SubCall;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

public class TestCaseHandler extends TbElementHandler {

	private boolean insideInteraction;
	private boolean insideSequence;
	private boolean insideParameterCombination;
	private String lastInteractionRef;
	private ArrayList<Link> interactionParameters;
	private ArrayList<SubCall> subCalls;
	private ArrayList<InteractionParameter> parameters;
	private String lastDataTypeRef;
	private ArrayList<Link> parameterValues;

	public TestCaseHandler(EndOfRedirect endOfRedirect, List path) {
		super(endOfRedirect, path);
		this.subCalls = new ArrayList<SubCall>();
		this.parameters = new ArrayList<InteractionParameter>();
		this.parameterValues = new ArrayList<Link>();
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("interaction")) {
			insideInteraction = true;
		} else if (localName.equals("call-sequence")) {
			insideInteraction = false;
			insideSequence = true;
		} else if (localName.equals("parameter-combination")) {
			insideInteraction = false;
			insideSequence = false;
			insideParameterCombination = true;
		} else if (localName.equals("interaction-ref")) {
			lastInteractionRef = attributes.getValue("pk");
			interactionParameters = new ArrayList<Link>();
		} else if (localName.equals("datatype-ref")) {
			lastDataTypeRef = attributes.getValue("pk");
		} else if (localName.equals("representative-ref")) {
			if (!insideParameterCombination) {
			interactionParameters.add(new Link(attributes.getValue("pk"),
					LinkType.REPRESENTATIVE));
			} else {
				parameterValues.add(new Link(attributes.getValue("pk"),LinkType.REPRESENTATIVE));
			}
		} else if (localName.equals("parent-parameter-ref")) {
			interactionParameters.add(new Link(attributes.getValue("pk"),
					LinkType.PARAMETER));
		}

	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("interaction")) {
			insideInteraction = false;
		} else if (localName.equals("call-sequence")) {
			insideSequence = false;
		} else if (localName.equals("parameter-combination")) {
			insideParameterCombination = false;
		} else if (localName.equals("interaction-call")) {
			this.subCalls.add(new SubCall(lastInteractionRef,
					interactionParameters));
		} else if (localName.equals("parameter")) {
			parameters.add(new InteractionParameter(getInnerName(),
					getInnerPk(), new Link(lastDataTypeRef,
							LinkType.REPRESENTATIVE)));
		}

	}

	@Override
	public TbElement getTbElement() {
		return new TestSatz(getName(), getPathAsStringList(), getPk(),
				parameters, subCalls,parameterValues);
	}

}
