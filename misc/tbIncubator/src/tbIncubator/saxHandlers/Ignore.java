package tbIncubator.saxHandlers;

import java.util.Collections;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler;
import tbIncubator.domain.TbElement;

public class Ignore extends TbElementHandler<TbElement> {

	public Ignore(tbIncubator.TbElementHandler.EndOfRedirect endOfRedirect) {
		super(endOfRedirect, Collections.EMPTY_LIST);
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public TbElement getTbElement() {
		return null;
	}
	
	@Override
	protected Set<String> getIgnore() {
		return Collections.EMPTY_SET;
	}

}
