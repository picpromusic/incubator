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

public class Parser {

	public interface EndOfRedirect {

		void end();

	}

	public static class DataTypeHandler extends DefaultHandler {

		private EndOfRedirect endOfRedirect;
		private int depth;
		private StringBuilder sb = new StringBuilder();
		private String name;
		private List<SubdivisionInfo> path;
		private String pk;
		private String innerPk;
		private String innerName;
		private List<Representative> representatives;
		private List<Link> representativeLinks;
		private List<Link> fieldLinks;

		public DataTypeHandler(EndOfRedirect endOfRedirect,
				List<SubdivisionInfo> path) {
			this.endOfRedirect = endOfRedirect;
			this.representatives = new ArrayList<Representative>();
			this.representativeLinks = new ArrayList<Link>();
			this.fieldLinks = new ArrayList<Link>();
			this.path = path;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			sb.setLength(0);

			if (localName.equals("representative-ref")) {
				representativeLinks.add(new Link(attributes.getValue("pk")));
			} else if (localName.equals("datatype-ref")) {
				fieldLinks.add(new Link(attributes.getValue("pk"), innerName));
			}

			depth++;
			// DO IT HERE
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);

			if (localName.equals("name") && name == null) {
				this.name = sb.toString();
			} else if (localName.equals("name")) {
				this.innerName = sb.toString();
			} else if (localName.equals("pk") && pk == null) {
				this.pk = sb.toString();
			} else if (localName.equals("pk")) {
				innerPk = sb.toString();
			} else if (localName.equals("representative")) {
				representatives.add(new Representative(innerName, innerPk,
						representativeLinks));
				representativeLinks = new ArrayList<Link>();
			}
			// DO IT HERE

			sb.setLength(0);
			depth--;
			if (depth == 0) {
				endOfRedirect.end();
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			sb.append(ch, start, length);
		}

		public DataType getDataType() {
			ArrayList<String> path = new ArrayList<String>(this.path.size());
			for (SubdivisionInfo si : this.path) {
				path.add(si.name);
			}
			DataType dt = new DataType(name, path, pk, representatives,
					fieldLinks);
			for (Representative rep : representatives) {
				rep.setDefinedIn(dt);
			}
			return dt;
		}

	}

	private static class SubdivisionInfo {
		public final String name;
		public final int depth;

		private SubdivisionInfo(String name, int depth) {
			this.name = name;
			this.depth = depth;
		}

		@Override
		public String toString() {
			return name;
		}
	}

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
			private DefaultHandler redirectTo;
			private List<DataType> datatypes = new ArrayList<DataType>(500);

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
						if (redirectTo instanceof DataTypeHandler) {
							datatypes.add(((DataTypeHandler) redirectTo)
									.getDataType());
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
				HashMap<String, Representative> index = new HashMap<String, Representative>();
				HashMap<String, DataType> typeIndex = new HashMap<String, DataType>();
				for (DataType dataType : datatypes) {
					typeIndex.put(dataType.pk, dataType);
					for (Representative rep : dataType.representatives) {
						index.put(rep.pk, rep);
					}
				}

				for (DataType dataType : datatypes) {
					String javaName = dataType.toJavaName();
					if (javaName.startsWith("Neues")) {
						try {
							File file = new File("gen-src/"
									+ javaName.replaceAll("\\.", "/") + ".java");
							file.getParentFile().mkdirs();
							PrintWriter pw = new PrintWriter(file);

							pw.println("package " + dataType.getPackage() + ";");

							pw.println("public enum "
									+ dataType.getSimpleName() + "{");
							System.out.println(dataType.getSimpleName());

							for (Representative rep : dataType.representatives) {
								pw.print("\t" + rep.toJavaName());
								StringBuilder sb = new StringBuilder();
								for (Link link : rep.representativeLinks) {
									Representative representative = index
											.get(link.ref);
									String jName = representative
											.getDefinedIn().toJavaName();
									jName = shortenPackage(dataType, jName);
									sb.append(jName);
									sb.append(".");
									sb.append(representative.toJavaName());
									sb.append(",");
								}
								if (sb.length() > 0) {
									sb.setLength(sb.length() - 1);
									pw.print("(");
									pw.print(sb.toString());
									pw.print(")");
								} else {
									pw.print("(\"");
									pw.print(rep.name);
									pw.print("\")");
								}
								pw.println(",//");
							}
							pw.println(";");

							StringBuilder ctorDef = new StringBuilder();
							ctorDef.append("\t");
							ctorDef.append(dataType.getSimpleName());
							ctorDef.append("(");
							StringBuilder ctorImpl = new StringBuilder();
							ctorImpl.append("\t{\n");
							for (Link link : dataType.fieldLinks) {
								String name = link.name.substring(0, 1)
										.toLowerCase() + link.name.substring(1);

								String jName = typeIndex.get(link.ref)
										.toJavaName();
								jName = shortenPackage(dataType, jName);
								pw.println("\tpublic final " + jName + " "
										+ name + ";");
								ctorDef.append(jName);
								ctorDef.append(" ");
								ctorDef.append(name);
								ctorDef.append(",");
								ctorImpl.append("\t\t");
								ctorImpl.append("this.");
								ctorImpl.append(name);
								ctorImpl.append("=");
								ctorImpl.append(name);
								ctorImpl.append(";\n");
							}
							if (dataType.fieldLinks.isEmpty()) {
								pw.println("\tpublic final String textRepresentation;");
								ctorDef.append("String textRepresentation,");
								ctorImpl.append("\t\tthis.textRepresentation = textRepresentation;\n");
							}

							pw.println("\n");

							ctorDef.setLength(ctorDef.length() - 1);
							ctorDef.append(")");
							ctorImpl.append("\t}");

							pw.println(ctorDef.toString());
							pw.println(ctorImpl.toString());

							pw.println("}");
							pw.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			private String shortenPackage(DataType dataType, String jName) {
				if (jName.startsWith(dataType.getPackage())) {
					jName = jName.substring(dataType.getPackage().length() + 1);
				}
				return jName;
			}
		};
	}
}
