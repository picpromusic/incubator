public enum ExceptionExpectedOn {
	GET, PUT, GETSTATIC, PUTSTATIC;

	public void throwRTE() {
		throw new RTE(this);
	}

	public void throwCHECKED() throws Throwable {
		throw new CHECKED(this);
	}

	public static class RTE extends RuntimeException {
		private final ExceptionExpectedOn why;

		public ExceptionExpectedOn getWhy() {
			return why;
		}

		private RTE(ExceptionExpectedOn why) {
			this.why = why;
		}
	}

	public static class CHECKED extends Exception {
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