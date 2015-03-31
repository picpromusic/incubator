package tbIncubator;

import java.util.List;

import org.xml.sax.Attributes;

import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.TbElementHandler.SubdivisionInfo;

public interface SubTypeHandlerFactory {

	boolean canHandle(String uri, String localName, String qName,
			Attributes attributes);

	TbElementHandler<?> create(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> pathAsList);

}
