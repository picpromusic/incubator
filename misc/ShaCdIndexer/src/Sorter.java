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

public class Sorter {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		String line = bufr.readLine();
		Map<String, List<String>> content = new TreeMap<>();
		while (line != null) {
			String[] split = line.split("!");
//			System.out.println(split[0]);
			List<String> list = content.get(split[0]);
			if (list == null) {
				list = new LinkedList<>();
				content.put(split[0], list);
			}
			list.add(line);
			line = bufr.readLine();
		}
		bufr.close();
		System.out.println(content.size());
		PrintWriter pw = new PrintWriter(new FileWriter(args[0]+".sorted.txt"));
		PrintWriter pw2 = new PrintWriter(new FileWriter(args[0]+".doppelte.txt"));
		for(Entry<String, List<String>> s : content.entrySet()) {
			List<String> value = s.getValue();
			if (value.size() > 1) {
				pw2.println(value.size() + " Vorkommnisse von " + s.getKey());
				for (String string : value) {
					pw2.println(string);
				}
			}
			for (String string : value) {
				pw.println(string);
			}
		}
		pw.close();
		pw2.close();
	}
}
