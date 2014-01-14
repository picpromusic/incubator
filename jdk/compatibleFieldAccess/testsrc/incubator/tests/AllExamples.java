package incubator.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

public class AllExamples {

	public static void main(String[] args) {
		PrintStream out = System.out;
		Buffer b1 = new Buffer();
		Buffer b2 = new Buffer();
		Buffer b3 = new Buffer();
		Buffer b4 = new Buffer();
		Buffer b5 = new Buffer();
		Buffer b6 = new Buffer();
		boolean erg1 = test(Example1.class, args, b1);
		boolean erg2 = test(Example2.class, args, b2);
		boolean erg3 = test(Example3.class, args, b3);
		boolean erg4 = test(Example4.class, args, b4);
		boolean erg5 = test(Example5.class, args, b5);
		boolean erg6 = test(Example6.class, args, b6);
		System.setOut(out);
		printErg(Example1.class, erg1, b1);
		printErg(Example2.class, erg2, b2);
		printErg(Example3.class, erg3, b3);
		printErg(Example4.class, erg4, b4);
		printErg(Example5.class, erg5, b5);
		printErg(Example6.class, erg6, b6);
	}

	private static void printErg(Class<?> clazz, boolean erg1, Buffer buffer) {
		if (erg1) {
			System.out.println(clazz + " OK");
		} else {
			System.out.println(clazz + " NOK");
			System.out.println(buffer.toString());
		}
	}

	private static boolean test(Class<?> clazz, String[] args, Buffer buffer) {
		System.setOut(buffer);
		try {
			clazz.getMethod("main", String[].class).invoke(null, (Object)args);
			return true;
		} catch (Throwable th) {
			return false;
		} finally {
			buffer.close();
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
