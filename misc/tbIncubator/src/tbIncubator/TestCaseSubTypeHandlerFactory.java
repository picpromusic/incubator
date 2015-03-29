package tbIncubator;

import java.util.List;

import org.xml.sax.Attributes;

import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.TbElementHandler.SubdivisionInfo;
import tbIncubator.saxHandlers.TestCaseHandler;

public class TestCaseSubTypeHandlerFactory implements SubTypeHandlerFactory {

	@Override
	public boolean canHandle(String uri, String localName, String qName,
			Attributes attributes) {
		return localName.equals("testcase");
	}

	@Override
	public TbElementHandler<?> create(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> pathAsList) {
		return new TestCaseHandler(endOfRedirect, pathAsList);
	}

}
