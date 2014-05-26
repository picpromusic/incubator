package incubator.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class AllExamples {

	public static final boolean PRINT_ON_OK = false;
	private static final int MAX = 7;

	public static void main(String[] args) {
		PrintStream out = System.out;
		List<ExampleRun> runner = new ArrayList<>();
		for (int i = 1; i <= MAX; i++) {
			runner.add(new ExampleRun(i).test(args));
		}
		for (ExampleRun exampleRun : runner) {
			exampleRun.printResult();
		}
	}

	public static class ExampleRun {
		public final Buffer b;
		public final int num;
		public boolean result;

		public ExampleRun(int num) {
			this.num = num;
			b = new Buffer();
			result = false;
		}

		public ExampleRun test(String[] args) {
			PrintStream old = System.out;
			try {
				System.setOut(b);
				Class clazz = Class.forName(getClassName());
				clazz.getMethod("main", String[].class).invoke(null,
						(Object) args);
				result = true;
			} catch (Throwable th) {
				result = false;
				th.printStackTrace(b);
			} finally {
				b.close();
				System.setOut(old);
			}
			return this;
		}

		private String getClassName() {
			return "incubator.tests.Example" + num;
		}

		public void printResult() {
			if (result) {
				System.out.println(getClassName() + " OK");
				if (PRINT_ON_OK) {
					System.out.println(b.toString());
				}
			} else {
				System.out.println(getClassName() + " NOK");
				System.out.println(b.toString());
			}
		}

	}

	public static class Buffer extends PrintStream {

		private ByteArrayOutputStream buffer;

		@Override
		public String toString() {
			return buffer.toString();
		}

		public Buffer() {
			super(new ByteArrayOutputStream());
			this.buffer = new ByteArrayOutputStream();
		}

		public void write(byte[] b) throws IOException {
			buffer.write(b);
		}

		public void write(int b) {
			buffer.write(b);
		}

		public void write(byte[] b, int off, int len) {
			buffer.write(b, off, len);
		}

		public void writeTo(OutputStream out) throws IOException {
			buffer.writeTo(out);
		}

		public void flush() {
			try {
				buffer.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void reset() {
			buffer.reset();
		}

		public byte[] toByteArray() {
			return buffer.toByteArray();
		}

		public int size() {
			return buffer.size();
		}

		public void close() {
			try {
				buffer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
