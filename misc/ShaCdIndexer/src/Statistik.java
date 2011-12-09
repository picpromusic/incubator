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

public class Statistik {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		FileField sizer = FileField.SIZE;
		long sizeGrep = 0;
		long sizeGrepInv = 0;
		long sizeGrepNon = 0;
		int countGrep = 0;
		int countGrepInv = 0;
		int countGrepNon = 0;
		String firstline = bufr.readLine();
		String line = bufr.readLine();
		while (line != null) {
			String[] split = line.split("\\|");
			Long size = (Long) sizer.getParser()
					.toObject(split[sizer.getPos()]);
			switch (line.charAt(0)) {
			case '+':
				countGrep++;
				sizeGrep += size;
				break;
			case '-':
				countGrepInv++;
				sizeGrepInv += size;
				break;
			default:
				countGrepNon++;
				sizeGrepNon += size;
				break;
			}
			line = bufr.readLine();
		}
		bufr.close();
		System.out.println(countGrep);
		System.out.println(sizeGrep);
		System.out.println();
		System.out.println(countGrepInv);
		System.out.println(sizeGrepInv);
		System.out.println();
		System.out.println(countGrepNon);
		System.out.println(sizeGrepNon);
	}
}
