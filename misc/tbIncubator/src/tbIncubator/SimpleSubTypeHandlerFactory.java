package tbIncubator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import tbIncubator.MyTbElementHandler.EndOfRedirect;
import tbIncubator.MyTbElementHandler.SubdivisionInfo;

final class SimpleSubTypeHandlerFactory implements
		SubTypeHandlerFactory {
	private final Class<? extends MyTbElementHandler> clazz;
	private final String type;

	SimpleSubTypeHandlerFactory(
			String type, Class<? extends MyTbElementHandler> clazz) {
		this.clazz = clazz;
		this.type = type;
	}

	@Override
	public MyTbElementHandler<?> create(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> pathAsList) {
		try {
			return clazz.getConstructor(EndOfRedirect.class,
					List.class).newInstance(endOfRedirect,
					pathAsList);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canHandle(String uri, String localName,
			String qName, Attributes attributes) {
		return localName.equals("element")
				&& attributes.getValue("type").equals(type);
	}
}