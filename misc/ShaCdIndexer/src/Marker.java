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

public class Marker {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		FileField f = FileField.valueOf(args[1]);
		String firstline = bufr.readLine();
		String line = bufr.readLine();
		String dest = args[0] + ".marked.txt";
		Pattern regEx = Pattern.compile(args[2]);
		PrintWriter pw = new PrintWriter(new FileWriter(dest));
		pw.println(firstline);
		boolean plusMinus = args[3].trim().length() == 1;
		while (line != null) {
			boolean optionalLine = line.startsWith(" ");
			boolean canBeMarked = plusMinus && optionalLine;
			boolean canBeUnmarked = !plusMinus && !optionalLine;
			if (canBeMarked || canBeUnmarked) {
				String[] split = line.split("\\|");
				// System.out.println(split[1]);
				String value = split[f.getPos()];
				if (regEx.matcher(value).matches()) {
					// System.out.print(args[3]);
					// System.out.println(line.substring(1));
					pw.print(args[3]);
					pw.println(line.substring(1));
				} else {
					pw.println(line);
				}
			} else {
				pw.println(line);
			}
			line = bufr.readLine();
		}
		bufr.close();
		pw.close();
		if (args[0].endsWith(".marked.txt")) {
			File src = new File(args[0]);
			src.delete();
			new File(dest).renameTo(src);
		}

	}
}
