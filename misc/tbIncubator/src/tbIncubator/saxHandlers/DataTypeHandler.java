package tbIncubator.saxHandlers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import tbIncubator.TbElementHandler;
import tbIncubator.TbElementHandler.EndOfRedirect;
import tbIncubator.TbElementHandler.SubdivisionInfo;
import tbIncubator.domain.DataType;
import tbIncubator.domain.Link;
import tbIncubator.domain.Representative;
import tbIncubator.domain.Link.LinkType;

public class DataTypeHandler extends TbElementHandler<DataType> {

	private List<Representative> representatives;
	private List<Link> representativeLinks;
	private List<Link> fieldLinks;

	public DataTypeHandler(EndOfRedirect endOfRedirect,
			List<SubdivisionInfo> path) {
		super(endOfRedirect, path);
		this.representatives = new ArrayList<Representative>();
		this.representativeLinks = new ArrayList<Link>();
		this.fieldLinks = new ArrayList<Link>();
	}

	@Override
	public void innerStartElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("representative-ref")) {
			representativeLinks.add(new Link(attributes.getValue("pk"),
					LinkType.REPRESENTATIVE));
		} else if (localName.equals("datatype-ref")) {
			fieldLinks.add(new Link(attributes.getValue("pk"), super
					.getInnerName(), LinkType.REPRESENTATIVE));
		}

	}

	@Override
	public void innerEndElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equals("representative")) {
			representatives.add(new Representative(getInnerName(),
					getInnerPk(), representativeLinks));
			representativeLinks = new ArrayList<Link>();
		}
		// DO IT HERE

	}

	@Override
	public DataType getTbElement() {
		ArrayList<String> path = getPathAsStringList();
		DataType dt = new DataType(getName(), path, getPk(), representatives,
				fieldLinks);
		for (Representative rep : representatives) {
			rep.setDefinedIn(dt);
		}
		return dt;
	}

}