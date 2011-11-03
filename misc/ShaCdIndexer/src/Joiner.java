import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Joiner {
	public static void main(String[] args) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter("joined.txt"));
		for (String f : args) {
			File src = new File(f);
			if (src.exists() && src.isFile()) {
				System.out.println(src.getAbsolutePath());
				BufferedReader bufr = new BufferedReader(new FileReader(src));
				String line = bufr.readLine();
				while (line != null) {
					pw.println(line);
					line = bufr.readLine();
				}
				bufr.close();
			}
		}
		pw.close();
	}
}
