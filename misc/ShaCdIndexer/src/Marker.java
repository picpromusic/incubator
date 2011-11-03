import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Marker {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		String line = bufr.readLine();
		Map<String, List<String>> content = new TreeMap<>();
		String dest = args[0]+".marked.txt";
		if (args[0].endsWith(".marked.txt")) {
			dest = args[0];
		}
		PrintWriter pw = new PrintWriter(new FileWriter(dest));
		while (line != null) {
			String[] split = line.split("!");
//			System.out.println(split[1]);
			if (split[1].matches(args[1])) {
				System.out.print(args[2]);
				System.out.println(line.substring(1));
				pw.print(args[2]);
				pw.println(line.substring(1));
			}else {
				pw.println(line);
			}
			line = bufr.readLine();
		}
		bufr.close();
		System.out.println(content.size());
		for(Entry<String, List<String>> s : content.entrySet()) {
			List<String> value = s.getValue();
			for (String string : value) {
				pw.println(string);
			}
		}
		pw.close();
	}
}
