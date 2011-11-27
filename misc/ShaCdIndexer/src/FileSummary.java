import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

public class FileSummary {

	private final boolean includeInBackup;
	private String backupMediumName;
	private Path globBaseDir;
	private Path relPath;
	private long length;
	private long lastModified;
	private byte[] hash;
	private static DateFormat formatter;
	
	static {
		formatter = DateFormat.getDateTimeInstance(DateFormat.FULL,
				DateFormat.FULL, Locale.GERMAN);
	}

	public FileSummary(boolean b, String dvdName, Path globBaseDir, Path path,
			long length, long lastModified, byte[] hash) {
		this.includeInBackup = b;
		this.backupMediumName = dvdName;
		this.globBaseDir = globBaseDir;
		this.relPath = path;
		this.length = length;
		this.lastModified = lastModified;
		this.hash = hash;
	}

	public void printToTxt(PrintWriter pw) {
		pw.print(includeInBackup ? "+" : "-");
		pw.print(getHashAsTxt());
		pw.print("|");
		pw.print(backupMediumName);
		pw.print("/");
		pw.print(globBaseDir.relativize(relPath));
		pw.print("|");
		pw.print(length);
		pw.print("|");
		pw.print(formatter.format(lastModified));
		pw.println();
	}

	protected String getHashAsTxt() {
		return Base64.encodeBase64String(hash);
	}

	public long getFileSize() {
		return this.length;
	}

}
