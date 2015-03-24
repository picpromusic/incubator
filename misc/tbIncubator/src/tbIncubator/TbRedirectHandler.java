package tbIncubator;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.saxHandlers.Ignore;

public abstract class TbRedirectHandler extends TbDumpPathHandler {

	protected abstract void setupSubTypeHandlerFactories();

	protected abstract EndOfRedirect endOfRedirect();

	private TbElementHandler<?> redirectTo;
	private Set<SubTypeHandlerFactory> subTypeHandlerFactories = new HashSet<SubTypeHandlerFactory>();

	private static final Set<String> IGNORE = new TreeSet<String>();

	static {
		IGNORE.add("old-versions");
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		setupSubTypeHandlerFactories();
		System.out.println("Los gehts");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (redirectTo != null) {
			redirectTo.startElement(uri, localName, qName, attributes);
		} else if (getIgnore().contains(localName)) {
			redirectTo = new Ignore(endOfRedirect());
			redirectTo.startElement(uri, localName, qName, attributes);
		} else {
			for (SubTypeHandlerFactory sthf : subTypeHandlerFactories) {
				if (sthf.canHandle(uri, localName, qName, attributes)) {
					redirectTo = sthf.create(this.endOfRedirect(),
							getPathAsList());
					redirectTo.startElement(uri, localName, qName, attributes);
					return;
				}
			}

			super.startElement(uri, localName, qName, attributes);
		}
	}

	protected Set<String> getIgnore() {
		return IGNORE;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (redirectTo != null) {
			redirectTo.characters(ch, start, length);
		} else {
			super.characters(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (redirectTo != null) {
			redirectTo.endElement(uri, localName, qName);
		} else {
			super.endElement(uri, localName, qName);
		}

	}

	protected void clearRedirect() {
		redirectTo = null;
	}

	protected void setup(SubTypeHandlerFactory subTypeHandlerFactory) {
		subTypeHandlerFactories.add(subTypeHandlerFactory);
	}

	protected void setupSimple(String type,
			Class<? extends TbElementHandler> clazz) {
		setup(new SimpleSubTypeHandlerFactory(//
				type, clazz));
	}

}
