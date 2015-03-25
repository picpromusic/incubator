package tbIncubator.saxHandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;

import tbIncubator.TbElementHandler;
import tbIncubator.TbRedirectHandler;
import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.domain.DataType;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

public class ProjectDumpHandler extends TbRedirectHandler {

	private List<DataType> datatypes = new ArrayList<DataType>(500);
	private List<Interaction> interactions = new ArrayList<Interaction>(500);
	private List<TestSatz> testsaetze = new ArrayList<TestSatz>(500);

	public List<DataType> getDatatypes() {
		return Collections.unmodifiableList(datatypes);
	}

	public List<Interaction> getInteractions() {
		return Collections.unmodifiableList(interactions);
	}

	@Override
	protected EndOfRedirect endOfRedirect() {
		return new EndOfRedirect() {

			@Override
			public void end(TbElementHandler handler) {

				TbElement tbElement = handler.getTbElement();
				if (tbElement instanceof DataType) {
					datatypes.add((DataType) tbElement);
				} else if (tbElement instanceof Interaction) {
					interactions.add((Interaction) tbElement);
				} else if (tbElement instanceof TestSatz) {
					testsaetze.add((TestSatz) tbElement);
				}

				clearRedirect();

			}

		};
	}

	@Override
	protected void setupSubTypeHandlerFactories() {
		setupSimple("interaction", InteractionHandler.class);
		setupSimple("datatype", DataTypeHandler.class);
		setupSimple("testcase", TestCaseHandler.class);
	}

	@Override
	protected boolean isPathElementStart(String localName, Attributes attributes) {
		return isStartOfSubdivision(localName, attributes)
				|| isStartOfTestTheme(localName);
	}

	private boolean isStartOfTestTheme(String localName) {
		return isTestThemeTag(localName);
	}

	private boolean isStartOfSubdivision(String localName, Attributes attributes) {
		return isElementTag(localName) && isSubdivisionType(attributes);
	}

	private boolean isSubdivisionType(Attributes attributes) {
		return attributes.getValue("type").equals("subdivision");
	}

	private boolean isTestThemeTag(String localName) {
		return localName.equals("testtheme");
	}

	private boolean isElementTag(String localName) {
		return localName.equals("element");
	}

	@Override
	protected boolean isPotencialEnd(String localName) {
		return isElementTag(localName) || isStartOfTestTheme(localName);
	}

	public List<TestSatz> getTestSaetze() {
		return testsaetze;
	}
}