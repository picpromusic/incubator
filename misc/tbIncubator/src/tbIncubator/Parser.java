package tbIncubator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import tbIncubator.generator.JavaCodeGeneratorPoet;
import tbIncubator.saxHandlers.ProjectDumpHandler;

public class Parser {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, FileNotFoundException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();

		File file = new File(args[0],"ks-src");
		file.mkdirs();
		
		ProjectDumpHandler projectDumpHandler = new ProjectDumpHandler();
		saxParser.parse(new FileInputStream(new File("project-dump.xml")),
				projectDumpHandler);

		try {
			// new
			// JavaCodeGeneratorWithStringBuilders(projectDumpHandler.getDatatypes(),
			// projectDumpHandler.getInteractions()).generate(file);
			new JavaCodeGeneratorPoet(projectDumpHandler.getDatatypes(),
					projectDumpHandler.getInteractions(),
					projectDumpHandler.getTestSaetze()).generate(file);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
