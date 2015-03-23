package tbIncubator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.TbElementHandler.SubdivisionInfo;

public abstract class TbRedirectHandler extends TbDumpPathHandler {

	protected abstract void setupSubTypeHandlerFactories();

	protected abstract EndOfRedirect endOfRedirect();

	private TbElementHandler<?> redirectTo;
	private Set<SubTypeHandlerFactory> subTypeHandlerFactories = new HashSet<SubTypeHandlerFactory>();

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
		}else {
			super.endElement(uri, localName, qName);
		}
		
	}

	protected void clearRedirect() {
		redirectTo = null;
	}

	protected void setup(SubTypeHandlerFactory subTypeHandlerFactory) {
		subTypeHandlerFactories.add(subTypeHandlerFactory);
	}

	protected void setupSimple(String type, Class<? extends TbElementHandler> clazz) {
		setup(new SimpleSubTypeHandlerFactory(//
				type, clazz));
	}

}
