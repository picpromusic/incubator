import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.junit.Test;

public class SummaryTests {

	@Test
	public void test() {
		Path path = Paths.get(".");
		Path parent = Paths.get("..");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		FileSummary fs = new FileSummary(true, "dvdName", parent, path, 10, 1000000,
				new byte[512 / 8]);
		fs.printToTxt(pw);
		pw.close();
		Assert.assertEquals("+AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAAAA" //
				+ "AAAAAA==" //
				+ "!dvdName/" //
				+ "../." //
				+ "!10" //
				+ "!Donnerstag, 1. Januar 1970 01:16 Uhr MEZ\n", //
				sw.getBuffer().toString());

		sw = new StringWriter();
		pw = new PrintWriter(sw);
		fs = new ErrorSummary("dvdName", parent, path, 512);
		fs.printToTxt(pw);
		pw.close();
		Assert.assertEquals("-" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "========" //
				+ "!dvdName/" //
				+ "../." //
				+ "!0" //
				+ "!Donnerstag, 1. Januar 1970 01:00 Uhr MEZ\n", //
				sw.getBuffer().toString());

	}

}
