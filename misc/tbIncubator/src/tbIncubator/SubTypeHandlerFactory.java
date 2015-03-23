package tbIncubator;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import tbIncubator.MyTbElementHandler.EndOfRedirect;
import tbIncubator.MyTbElementHandler.SubdivisionInfo;

public interface SubTypeHandlerFactory {

	boolean canHandle(String uri, String localName, String qName,
			Attributes attributes);

	MyTbElementHandler<?> create(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> pathAsList);

}
