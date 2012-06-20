package util;
public enum ExceptionExpectedOn {
	GET, PUT, GETSTATIC, PUTSTATIC;

	public void throwRTE() {
		throw new RUNTIME(this);
	}

	public void throwCHECKED() throws Throwable {
		throw new CHECKED(this);
	}

	public static final class RUNTIME extends RuntimeException {
		private final ExceptionExpectedOn why;

		public ExceptionExpectedOn getWhy() {
			return why;
		}

		private RUNTIME(ExceptionExpectedOn why) {
			this.why = why;
		}
	}

	public static final class CHECKED extends Exception {
		private final ExceptionExpectedOn why;

		public ExceptionExpectedOn getWhy() {
			return why;
		}

		private CHECKED(ExceptionExpectedOn why) {
			this.why = why;
			// TODO Auto-generated constructor stub
		}
	}
}