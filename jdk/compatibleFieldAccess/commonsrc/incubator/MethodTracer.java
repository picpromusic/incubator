package incubator;

import java.util.ArrayList;
import java.util.List;

public class MethodTracer {
	private static ThreadLocal<List<String>> traces = new ThreadLocal<List<String>>() {
		protected java.util.List<String> initialValue() {
			return new ArrayList<>(64);
		};
	};

	private static ThreadLocal<Integer> index = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 0;
		};
	};

	public static void trace(String trace) {
		traces.get().add(trace);
	}

	private static void clean() {
		traces.remove();
		index.remove();
	}

	public static void checkCallCountAndClean() {
		if (traces.get().size() != getNextIndex()) {
			System.out.println("Traced calls:");
			int index = 0;
			for (String trace : traces.get()) {
				System.out.println(index++ + ": " + trace);
			}
			throw new AssertionError("see messages");
		}
		clean();
	}

	private static int getNextIndex() {
		int next = index.get();
		index.set(next + 1);
		return next;
	}

	public static void check(String expected) {
		int i = getNextIndex();
		if (traces.get().size() <= i || !expected.equals(traces.get().get(i))) {
			System.out.println("Call to \"" + expected
					+ "\" expected at index " + i);
			System.out.println("Traced calls:");
			int index = 0;
			for (String trace : traces.get()) {
				System.out.println(index++ + ": " + trace);
			}
			throw new AssertionError("see messages");
		}
	}
}
