import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
	public static class FileVisitorHashImplementation extends
			SimpleFileVisitor<Path> {

		private static final int SIZE_1_MB = 1024 * 1024;
		private static final int SIZE_50_MB = SIZE_1_MB * 50;
		private long globread;
		private float globproz;
		private final Path globBaseDir;
		private final long size;
		private final PrintWriter pw;

		public FileVisitorHashImplementation(Path globBaseDir, long size,
				PrintWriter pw) {
			this.size = size;
			this.globBaseDir = globBaseDir;
			this.pw = pw;
			this.globread = 0;
			this.globproz = 0.01f;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			System.err.println(file + " \t" + exc);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {
			byte[] mdbytes = calcHash(path);
			if (mdbytes != null) {
				File file = path.toFile();
				FileSummary fs = new FileSummary(true, dvdName, globBaseDir,
						path, file.length(), file.lastModified(),
						mdbytes);
				fs.printToTxt(pw);
			} else {
				FileSummary fs = new ErrorSummary(dvdName,globBaseDir,path,512);
				fs.printToTxt(pw);
			}
			return FileVisitResult.CONTINUE;
		}

		private void printResult(boolean include, Path path, long length,
				long lastModified, String encodeBase64String) {
		}

		private byte[] calcHash(Path path) {
			File file = path.toFile();
			byte[] mdbytes = null;
			try (FileInputStream fin = new FileInputStream(file)) {
				long length = file.length();
				byte[] dataBytes = new byte[10 * SIZE_1_MB];
				if (singleFileBigEnoughForStatistik(length)) {
					System.out.println(globBaseDir.relativize(path));
				}

				long read = 0;
				float nextProz = 0.10f;
				int nread = 0;
				while ((nread = fin.read(dataBytes)) != -1) {
					md.update(dataBytes, 0, nread);
					read += nread;
					globread += nread;
					if (singleFileBigEnoughForStatistik(length)
							&& ((float) read) / length > nextProz) {
						System.out.println((int) (globproz * 100) + "% "
								+ (int) (nextProz * 100) + "%");
						nextProz += 0.1f;
					}
				}
				if (((float) globread) / size > globproz) {
					System.out.println((int) (globproz * 100) + "%");
					globproz += 0.01f;
				}
				mdbytes = md.digest();
				fin.close();
				if (singleFileBigEnoughForStatistik(length)) {
					System.out.println("100%");
				}
			} catch (IOException e) {
				System.err.println(path + " " + e);
			}
			return mdbytes;
		}

		private boolean singleFileBigEnoughForStatistik(long length) {
			return length > SIZE_50_MB;
		}

	}

	private static final class FileVisitorSizeImplementation extends
			SimpleFileVisitor<Path> {
		private long size;
		private long files;

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			size += file.toFile().length();
			files++;
			if (files % 10000 == 0) {
				System.out.print(".");
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			System.err.println(file + " \t" + exc);
			return FileVisitResult.CONTINUE;
		}

		public long getSize() {
			return this.size;
		}
	}

	private static Path globBaseDir;
	private static PrintWriter pw;
	private static MessageDigest md;
	private static long size;
	private static long globread;
	private static float globproz;
	private static String dvdName;
	private static boolean calcSize;

	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException {

		md = MessageDigest.getInstance("SHA-512");

		globBaseDir = Paths.get(args[0]);
		dvdName = args[1];
		calcSize = Boolean.parseBoolean(args[2]);
		String name = dvdName + ".sha.txt";
		System.out.println(name);
		pw = new PrintWriter(new FileWriter(name));
		if (calcSize) {
			size = calcSize(globBaseDir);
		} else {
			size = Long.MAX_VALUE;
		}
		globread = 0;
		globproz = 0.01f;
		System.out.println(size);
		FileVisitorHashImplementation fileVisitorHashImplementation = new FileVisitorHashImplementation(
				globBaseDir, size, pw);
		Files.walkFileTree(globBaseDir, fileVisitorHashImplementation);

		// doIt(globBaseDir);
		pw.close();
	}

	private static long calcSize(Path baseDir) throws IOException {
		FileVisitorSizeImplementation fileVisitorImplementation = new FileVisitorSizeImplementation();
		Path walkFileTree = Files.walkFileTree(baseDir,
				fileVisitorImplementation);
		return fileVisitorImplementation.getSize();

	}

}
