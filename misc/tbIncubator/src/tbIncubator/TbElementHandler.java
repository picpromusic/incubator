package tbIncubator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tbIncubator.domain.TbElement;
import tbIncubator.saxHandlers.Ignore;

public abstract class TbElementHandler<T extends TbElement> extends
		DefaultHandler {

	public static class SubdivisionInfo {
		public final String name;
		public final int depth;

		public SubdivisionInfo(String name, int depth) {
			this.name = name;
			this.depth = depth;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public interface EndOfRedirect {

		void end(TbElementHandler handler);

	}

	private static final Set<String> IGNORE = new TreeSet<String>();
	
	static {
		IGNORE.add("old-versions");
	}

	private EndOfRedirect justClear() {
		return new EndOfRedirect() {

			@Override
			public void end(TbElementHandler handler) {
				clearRedirect();
			}
		};
	}

	private TbElementHandler<?> redirectTo;
	private EndOfRedirect endOfRedirect;
	private int depth;
	private StringBuilder sb = new StringBuilder();
	private String name;
	protected final List<SubdivisionInfo> path;
	private String pk;
	private String innerPk;
	private String innerName;
	private Map<String, String> innerStringValues;

	public TbElementHandler(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> path) {
		this.endOfRedirect = endOfRedirect;
		this.path = Collections.unmodifiableList(path);
		this.innerStringValues = new HashMap<String, String>();
	}

	public abstract void innerStartElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (redirectTo != null) {
			redirectTo.startElement(uri, localName, qName, attributes);
		} else if (getIgnore().contains(localName)) {
			redirectTo = new Ignore(endOfRedirect());
			redirectTo.startElement(uri, localName, qName, attributes);
		} else {
			super.startElement(uri, localName, qName, attributes);
			sb.setLength(0);

			innerStartElement(uri, localName, qName, attributes);

			depth++;
		}
	}

	protected Set<String> getIgnore() {
		return IGNORE;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (redirectTo != null) {
			redirectTo.endElement(uri, localName, qName);
		} else {
			super.endElement(uri, localName, qName);
			innerStringValues.put(localName, sb.toString());
			if (localName.equals("name") && name == null) {
				this.name = sb.toString();
			} else if (localName.equals("name")) {
				this.innerName = sb.toString();
			} else if (localName.equals("pk") && pk == null) {
				this.pk = sb.toString();
			} else if (localName.equals("pk")) {
				innerPk = sb.toString();
			} else {
				innerEndElement(uri, localName, qName);
			}

			sb.setLength(0);
			depth--;
			if (depth == 0) {
				endOfRedirect.end(this);
			}
		}
	}

	protected void clearRedirect() {
		redirectTo = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (redirectTo != null) {
			redirectTo.characters(ch, start, length);
		} else {
			super.characters(ch, start, length);
			sb.append(ch, start, length);
		}
	}

	public String getInnerName() {
		return innerName;
	}

	public String getInnerPk() {
		return innerPk;
	}

	public String getName() {
		return name;
	}

	public String getPk() {
		return pk;
	}

	protected ArrayList<String> getPathAsStringList() {
		ArrayList<String> path = new ArrayList<String>(this.path.size());
		for (SubdivisionInfo si : this.path) {
			path.add(si.name);
		}
		return path;
	}

	protected String getInnerStringValue(String key) {
		return innerStringValues.get(key);
	}

	public abstract void innerEndElement(String uri, String localName,
			String qName) throws SAXException;

	public abstract T getTbElement();

	protected EndOfRedirect endOfRedirect() {
		return justClear();
	}

}
