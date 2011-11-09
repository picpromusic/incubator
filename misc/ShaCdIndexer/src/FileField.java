import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public enum FileField {
	HASH(0, String.class, StringComparator.INSTANCE, HashParser.INSTANCE), //
	PATH(1, String.class, StringComparator.INSTANCE, StringParser.INSTANCE), //
	RELPATH(1, String.class, StringComparator.INSTANCE, RelPathParser.INSTANCE), //
	FILENAME(1, String.class, StringComparator.INSTANCE, FilenameParser.INSTANCE), //
	SIZE(2, long.class, LongComparator.INSTANCE, LongParser.INSTANCE), //
	DATE(3, Date.class, DataComparator.INSTANCE, DateParser.INSTANCE)//
	;

	private int pos;
	private Comparator<Object> comparator;
	private Parser<Object> parser;

	private <T> FileField(int pos, Class<T> type, Comparator<T> comp,
			Parser<T> p) {
		this.pos = pos;
		this.comparator = (Comparator<Object>) comp;
		this.parser = (Parser<Object>) p;
	}

	public Parser<Object> getParser() {
		return parser;
	}

	public Comparator<Object> getComparator() {
		return comparator;
	}

	public int getPos() {
		return pos;
	}

	public interface Parser<T> {
		T toObject(String value);
	}

	private static class HashParser implements Parser<String> {

		public static final HashParser INSTANCE = new HashParser();

		@Override
		public String toObject(String value) {
			return value.substring(1);
		}

	}
	
	private static class StringComparator implements Comparator<String> {

		public static final StringComparator INSTANCE = new StringComparator();

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}

	}

	private static class StringParser implements Parser<String> {

		public static final StringParser INSTANCE = new StringParser();

		@Override
		public String toObject(String value) {
			return value;
		}

	}
	
	private static class RelPathParser implements Parser<String> {

		public static final RelPathParser INSTANCE = new RelPathParser();

		@Override
		public String toObject(String value) {
			return value.substring(value.indexOf("/")+1);
		}

	}
	
	private static class FilenameParser implements Parser<String> {

		public static final FilenameParser INSTANCE = new FilenameParser();

		@Override
		public String toObject(String value) {
			Path p = Paths.get(RelPathParser.INSTANCE.toObject(value));
			return p.getFileName().toString();
		}

	}	

	private static class LongComparator implements Comparator<Long> {

		public static final LongComparator INSTANCE = new LongComparator();

		@Override
		public int compare(Long o1, Long o2) {
			return o1.compareTo(o2);
		}

	}

	private static class LongParser implements Parser<Long> {

		public static final LongParser INSTANCE = new LongParser();

		@Override
		public Long toObject(String value) {
			return Long.parseLong(value);
		}

	}

	private static class DataComparator implements Comparator<Date> {

		public static final DataComparator INSTANCE = new DataComparator();

		@Override
		public int compare(Date o1, Date o2) {
			return o1.compareTo(o2);
		}

	}

	private static class DateParser implements Parser<Date> {

		public static final DateParser INSTANCE = new DateParser();
		private DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.FULL, DateFormat.FULL, Locale.GERMAN);

		@Override
		public Date toObject(String value) {
			try {
				return formatter.parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
