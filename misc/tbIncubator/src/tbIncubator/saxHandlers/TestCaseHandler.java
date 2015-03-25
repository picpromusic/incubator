package tbIncubator.saxHandlers;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler;
import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

public class TestCaseHandler extends TbElementHandler {

	public TestCaseHandler(EndOfRedirect endOfRedirect, List path) {
		super(endOfRedirect, path);
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public TbElement getTbElement() {
		return new TestSatz("", getPathAsStringList(), getPk());
	}

}
