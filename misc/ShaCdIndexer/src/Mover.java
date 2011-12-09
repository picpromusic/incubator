import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;


public class Mover {
	public static void main(String[] args) throws IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(args[0]));
		FileField relpath = FileField.RELPATH;
		FileField.Parser<Object> relPathParser = relpath.getParser();
		Path dir = Paths.get(args[1]);
		Path movedDir = dir.getParent().resolve(dir.getName(dir.getNameCount()-1)+".moved");
		String firstline = bufr.readLine();
		String line = bufr.readLine();
		while (line != null) {
			if (line.startsWith("-")) {
				String[] split = line.split("\\|");
				String path = (String)relPathParser.toObject(split[relpath.getPos()]);
				Path movedPos = movedDir.resolve(path);
				movedPos.getParent().toFile().mkdirs();
				dir.resolve(path).toFile().renameTo(movedPos.toFile());
				File parentSrcDir = dir.resolve(path).getParent().toFile();
				String[] list = parentSrcDir.list();
				if (list != null && list.length == 0) {
					parentSrcDir.delete();
					System.out.println(parentSrcDir + " deleted");
				}
			}
			line = bufr.readLine();
		}
		bufr.close();
	}
}
