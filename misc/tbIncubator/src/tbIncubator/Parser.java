package tbIncubator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.events.EndDocument;

import org.xml.sax.Attributes;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tbIncubator.MyDefaultHandler.EndOfRedirect;
import tbIncubator.MyDefaultHandler.SubdivisionInfo;

public class Parser {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, FileNotFoundException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		saxParser.parse(new FileInputStream(new File("project-dump.xml")),
				handleXml());
	}

	private static DefaultHandler handleXml() {
		return new DefaultHandler() {

			private Bereich bereich;
			private boolean findSubdivisionName = false;
			private boolean extractSubdivisionName = false;
			private StringBuilder sb = new StringBuilder();
			private Stack<SubdivisionInfo> path = new Stack<SubdivisionInfo>();
			private int depth;
			private int lastSubdivisionDepth;
			private MyDefaultHandler<?> redirectTo;
			private List<DataType> datatypes = new ArrayList<DataType>(500);
			private List<Interaction> interactions = new ArrayList<Interaction>(
					500);

			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
				System.out.println("Los gehts");
			}

			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				if (redirectTo != null) {
					redirectTo.startElement(uri, localName, qName, attributes);
				} else {
					super.startElement(uri, localName, qName, attributes);

					depth++;
					if (localName.equals("test-elements")) {
						System.out.println("test-elements");
						bereich = Bereich.TESTELEMENTS;
					} else if (localName.equals("testthemes")) {
						System.out.println("testthemens");
						bereich = Bereich.TESTTHEMES;
					} else if (localName.equals("element")) {
						String type = attributes.getValue("type");
						if (type.equals("subdivision")) {
							lastSubdivisionDepth = depth;
							findSubdivisionName = true;
						} else if (type.equals("datatype")) {
							redirectTo = new DataTypeHandler(
									this.endOfRedirect(),
									new ArrayList<SubdivisionInfo>(path));
							redirectTo.startElement(uri, localName, qName,
									attributes);
						} else if (type.equals("interaction")) {
							redirectTo = new InteractionHandler(
									this.endOfRedirect(),
									new ArrayList<SubdivisionInfo>(path));
							redirectTo.startElement(uri, localName, qName,
									attributes);
						}
					} else if (localName.equals("name") && findSubdivisionName) {
						findSubdivisionName = false;
						extractSubdivisionName = true;
						sb.setLength(0);
					}
				}
			}

			private EndOfRedirect endOfRedirect() {
				return new EndOfRedirect() {

					@Override
					public void end() {
						TbElement tbElement = redirectTo.getTbElement();
						if (tbElement instanceof DataType) {
							datatypes.add((DataType) tbElement);
						} else if (tbElement instanceof Interaction) {
							interactions.add((Interaction) tbElement);
						}
						redirectTo = null;
					}
				};
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

			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if (redirectTo != null) {
					redirectTo.endElement(uri, localName, qName);
				}

				if (redirectTo == null) {
					super.endElement(uri, localName, qName);
					if (localName.equals("test-elements")) {
						bereich = null;
					} else if (localName.equals("testthemes")) {
						bereich = null;
					} else if (extractSubdivisionName
							&& localName.equals("name")) {
						findSubdivisionName = false;
						extractSubdivisionName = false;
						path.push(new SubdivisionInfo(sb.toString(),
								lastSubdivisionDepth));
						System.out.println(path);
					} else if (localName.equals("element")
							&& path.peek().depth == depth) {
						path.pop();
					}
					sb.setLength(0);
					depth--;
				}
			}

			@Override
			public void endDocument() throws SAXException {
				super.endDocument();
				System.out.println("Fertig");

				File file = new File("gen-src");
				file.mkdirs();
				try {
//					new JavaCodeGeneratorWithStringBuilders(datatypes,
//							interactions).generate(file);
					new JavaCodeGeneratorPoet(datatypes,
							interactions).generate(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}
}
