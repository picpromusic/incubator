import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

public class Grep {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		FileField f = FileField.valueOf(args[1]);
		FileField sizer = FileField.SIZE;
		long sizeGrep = 0;
		long sizeGrepInv = 0;
		String firstline = bufr.readLine();
		String line = bufr.readLine();
		String dest = args[0] + ".greped.txt";
		String dest2 = args[0] + ".greped.others.txt";
		Pattern regEx = Pattern.compile(args[2]);
		PrintWriter pw = new PrintWriter(new FileWriter(dest));
		PrintWriter pw2 = new PrintWriter(new FileWriter(dest2));
		pw.println(firstline);
		pw2.println(firstline);
		while (line != null) {
			String[] split = line.split("\\|");
			// System.out.println(split[1]);
			String value = split[f.getPos()];
			Long size = (Long) sizer.getParser()
					.toObject(split[sizer.getPos()]);
			if (regEx.matcher(value).matches()) {
				sizeGrep += size;
				pw.println(line);
			} else {
				sizeGrepInv += size;
				pw2.println(line);
			}
			line = bufr.readLine();
		}
		bufr.close();
		pw.close();
		pw2.close();
		System.out.println(sizeGrep);
		System.out.println(sizeGrepInv);
	}
}
