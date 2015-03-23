package tbIncubator;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tbIncubator.MyTbElementHandler.SubdivisionInfo;

public abstract class TbDumpPathHandler extends DefaultHandler {

	private boolean findSubdivisionName = false;
	private boolean extractSubdivisionName = false;
	private StringBuilder sb = new StringBuilder();
	private Stack<SubdivisionInfo> path = new Stack<SubdivisionInfo>();
	private int depth;
	private int lastSubdivisionDepth;

	protected abstract boolean isPathElementStart(String localName, Attributes attributes);
	protected abstract boolean isPotencialEnd(String localName);

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		depth++;

		if (isPathElementStart(localName, attributes)) {
			lastSubdivisionDepth = depth;
			findSubdivisionName = true;
		} else if (localName.equals("name") && findSubdivisionName) {
			findSubdivisionName = false;
			extractSubdivisionName = true;
			sb.setLength(0);
		}
	}

	protected ArrayList<SubdivisionInfo> getPathAsList() {
		return new ArrayList<SubdivisionInfo>(path);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		sb.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (extractSubdivisionName && localName.equals("name")) {
			findSubdivisionName = false;
			extractSubdivisionName = false;
			path.push(new SubdivisionInfo(sb.toString(), lastSubdivisionDepth));
			System.out.println(path);
		} else if (isPotencialEnd(localName) && path.peek().depth == depth) {
			path.pop();
		}
		sb.setLength(0);
		depth--;
	}

}
