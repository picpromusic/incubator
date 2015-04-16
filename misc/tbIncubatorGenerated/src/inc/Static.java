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
		if (isLeerOderEgal(string)) {
			return null;
		}
		try {
			return df.parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Integer Anzahl(String string) {
		if (isLeerOderEgal(string)) {
			return null;
		}
		return Integer.parseInt(string.replaceAll("\\.", ""));
	}

	private static boolean isLeerOderEgal(String string) {
		return string.equals("<leer>") || string.equals("<egal>");
	}

	public static BigDecimal Betrag(String string) {
		try {
			if (isLeerOderEgal(string)) {
				return null;
			}
			string = string.replaceAll("\\.", "");
			string = string.replace(',', '.');
			BigDecimal bigDecimal = new BigDecimal(string);
			bigDecimal.setScale(2, RoundingMode.HALF_UP);
			return bigDecimal;
		} catch (RuntimeException e) {
			throw new RuntimeException("Fehler bei der Konvertierung von " + string + " in BigDecimal",e);
		}
	}

	public static BigDecimal Prozent(String string) {
		return null;
	}

}
