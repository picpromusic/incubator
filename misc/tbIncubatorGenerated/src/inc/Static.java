package inc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Static {

	private static final DateFormat df;

	static {
		df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
	}

	public static Date Datum(String string) {
		try {
			return df.parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Integer Anzahl(String string) {
		return Integer.parseInt(string.replaceAll("\\.", ""));
	}

	public static BigDecimal Betrag(String string) {
		BigDecimal bigDecimal = new BigDecimal(string);
		bigDecimal.setScale(2, RoundingMode.HALF_UP);
		return bigDecimal;
	}

	public static BigDecimal Prozent(String string) {
		return null;
	}

}
