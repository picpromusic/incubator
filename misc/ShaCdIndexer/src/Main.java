import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

	public static class FileVisitorHashImplementation extends
			SimpleFileVisitor<Path> implements AutoCloseable {

		private final class CompletedFuture<T> implements Future<T> {

			private T result;

			public CompletedFuture(T result) {
				this.result = result;
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}

			@Override
			public T get() throws InterruptedException, ExecutionException {
				return result;
			}

			@Override
			public T get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException,
					TimeoutException {
				return get();
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				return true;
			}
		}

		private static final int SIZE_1_MB = 1024 * 1024;
		private static final int SIZE_50_MB = SIZE_1_MB * 50;
		private static final long MIN_ASYNC_SIZE = 0 * SIZE_1_MB;
		private static final long MAX_ASYNC_SIZE = 256 * SIZE_1_MB;
		private static final int MAX_TOTAL_ASYNC_SIZE = 256 * SIZE_1_MB;

		private long globread;
		private float globproz;
		private final Path globBaseDir;
		private final long size;
		private final PrintWriter pw;
		private ConcurrentLinkedDeque<Future<FileSummary>> futures;
		private ExecutorService executorService;
		private long asyncSize;
		private int done;

		public FileVisitorHashImplementation(Path globBaseDir, long size,
				PrintWriter pw) {
			this.size = size;
			this.globBaseDir = globBaseDir;
			this.pw = pw;
			this.globread = 0;
			this.globproz = 0f;
			this.futures = new ConcurrentLinkedDeque<>();
			this.asyncSize = 0;
			this.done = 0;

			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors());

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
			checkFutures();
			Future<FileSummary> result = calcHash(path);
			if (result instanceof CompletedFuture && result.isDone()) {
				printSummary(result);
			} else {
				futures.add(result);
			}

			return FileVisitResult.CONTINUE;
		}

		private void checkFutures() {
			int todo = 0;
			int olddone = done;
			for (Future<FileSummary> result : futures) {
				if (result.isDone()) {
					done++;
					futures.remove(result);
					FileSummary fs = printSummary(result);
					asyncSize -= fs.getFileSize();
				} else {
					todo++;
				}
			}
			if (todo > 5) {
				System.out.println("DONE: " + done + " TO BE DONE:" + todo
						+ " ASYNC_BYTES_TODO:" + asyncSize);
			} else if((done / 1000) - (olddone / 1000) > 0) {
				for(int i = 0; i < (done / 1000) - (olddone / 1000); i++) {
					System.out.print(".");
				}
			}
		}

		private FileSummary printSummary(Future<FileSummary> result) {
			FileSummary fs = null;
			try {
				fs = result.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			if (fs != null) {
				synchronized (pw) {
					fs.printToTxt(pw);
				}
			} else {
				futures.add(result);
			}
			return fs;
		}

		private void printResult(boolean include, Path path, long length,
				long lastModified, String encodeBase64String) {
		}

		private Future<FileSummary> calcHash(final Path path) {
			final File file = path.toFile();
			byte[] mdbytes = null;
			final long length = file.length();
			if (length > MIN_ASYNC_SIZE && length < MAX_ASYNC_SIZE
					&& asyncSize + length < MAX_TOTAL_ASYNC_SIZE) {
				asyncSize += length;
				final ByteArrayOutputStream bout = new ByteArrayOutputStream();
				try (FileInputStream fin = new FileInputStream(file)) {
					byte[] buffer = new byte[10 * SIZE_1_MB];
					int read = fin.read(buffer);
					while (read > 0) {
						bout.write(buffer, 0, read);
						read = fin.read(buffer);
					}
					bout.close();
					return executorService.submit(new Callable<FileSummary>() {

						@Override
						public FileSummary call() throws Exception {
							MessageDigest md = MessageDigest
									.getInstance("SHA-512");
							md.update(bout.toByteArray());
							byte[] mdbytes = md.digest();

							return new FileSummary(true, dvdName, globBaseDir,
									path, length, file.lastModified(), mdbytes);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new CompletedFuture<FileSummary>(new ErrorSummary(
							dvdName, globBaseDir, path, 512));
				}
			} else {
				try (FileInputStream fin = new FileInputStream(file)) {
					byte[] dataBytes = new byte[10 * SIZE_1_MB];
					if (singleFileBigEnoughForStatistik(length)) {
						System.out.println(globBaseDir.relativize(path));
					}

					long read = 0;
					float nextProz = 0f;
					int nread = 0;
					while ((nread = fin.read(dataBytes)) != -1) {
						md.update(dataBytes, 0, nread);
						read += nread;
						globread += nread;
						if (singleFileBigEnoughForStatistik(length)) {
							if (((float) read) / length > nextProz) {
								nextProz += 0.1f;
								System.out.println((int) (globproz * 100)
										+ "% " + (int) (nextProz * 100) + "%");
							}
							if (((float) globread) / size > globproz) {
								globproz += 0.01f;
								System.out
										.println((int) (globproz * 100) + "%");
							}
						}
					}
					if (((float) globread) / size > globproz) {
						globproz += 0.01f;
						System.out.println((int) (globproz * 100) + "%");
					}
					mdbytes = md.digest();
					fin.close();
					if (singleFileBigEnoughForStatistik(length) && nextProz < 1) {
						System.out.println((int) (globproz * 100) + "% 100%");
					}
				} catch (IOException e) {
					System.err.println(path + " " + e);
				}
				FileSummary fs1;
				if (mdbytes != null) {
					fs1 = new FileSummary(true, dvdName, globBaseDir, path,
							length, file.lastModified(), mdbytes);
				} else {
					fs1 = new ErrorSummary(dvdName, globBaseDir, path, 512);
				}
				FileSummary fs = fs1;
				return new CompletedFuture<FileSummary>(fs);
			}
		}

		private FileSummary syncHash(Path path, File file, byte[] mdbytes) {
			try (FileInputStream fin = new FileInputStream(file)) {
				long length = file.length();
				byte[] dataBytes = new byte[10 * SIZE_1_MB];
				if (singleFileBigEnoughForStatistik(length)) {
					System.out.println(globBaseDir.relativize(path));
				}

				long read = 0;
				float nextProz = 0f;
				int nread = 0;
				while ((nread = fin.read(dataBytes)) != -1) {
					md.update(dataBytes, 0, nread);
					read += nread;
					globread += nread;
					if (singleFileBigEnoughForStatistik(length)) {
						if (((float) read) / length > nextProz) {
							nextProz += 0.1f;
							System.out.println((int) (globproz * 100) + "% "
									+ (int) (nextProz * 100) + "%");
						}
						if (((float) globread) / size > globproz) {
							globproz += 0.01f;
							System.out.println((int) (globproz * 100) + "%");
						}
					}
				}
				if (((float) globread) / size > globproz) {
					globproz += 0.01f;
					System.out.println((int) (globproz * 100) + "%");
				}
				mdbytes = md.digest();
				fin.close();
				if (singleFileBigEnoughForStatistik(length) && nextProz < 1) {
					System.out.println((int) (globproz * 100) + "% 100%");
				}
			} catch (IOException e) {
				System.err.println(path + " " + e);
			}
			FileSummary fs;
			if (mdbytes != null) {
				fs = new FileSummary(true, dvdName, globBaseDir, path,
						file.length(), file.lastModified(), mdbytes);
			} else {
				fs = new ErrorSummary(dvdName, globBaseDir, path, 512);
			}
			return fs;
		}

		private boolean singleFileBigEnoughForStatistik(long length) {
			return length > SIZE_50_MB;
		}

		@Override
		public void close() throws Exception {
			executorService.shutdown();
			while (!futures.isEmpty()) {
				checkFutures();
				TimeUnit.SECONDS.sleep(1);
			}
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
		pw.println("|V1.0|");
		if (calcSize) {
			size = calcSize(globBaseDir);
		} else {
			size = Long.MAX_VALUE;
		}
		globread = 0;
		globproz = 0.01f;
		System.out.println(size);
		try (FileVisitorHashImplementation fileVisitorHashImplementation = new FileVisitorHashImplementation(
				globBaseDir, size, pw)) {
			Files.walkFileTree(globBaseDir, fileVisitorHashImplementation);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
