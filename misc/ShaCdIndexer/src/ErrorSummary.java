import java.nio.file.Path;


public class ErrorSummary extends FileSummary {

	private int hashSize;

	public ErrorSummary(String dvdName, Path globBaseDir, Path path, int hashSize) {
		super(false, dvdName, globBaseDir, path, 0, 0, new byte[0]);
		this.hashSize = hashSize;
	}
	
	@Override
	protected String getHashAsTxt() {
		int chunks = hashSize / 6;
		StringBuilder sb = new StringBuilder("====");
		while (sb.length() < chunks) {
			sb.append("====");
		}
		return sb.toString();
	}
	
}
