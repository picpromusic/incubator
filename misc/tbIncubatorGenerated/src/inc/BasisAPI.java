package inc;

import inc.impl.InteraktionenImpl;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

public class BasisAPI {


	protected static Date Datum(String string) {
		return Static.Datum(string);
	}

	protected static Integer Anzahl(String string) {
		return Static.Anzahl(string);
	}

	protected static BigDecimal Betrag(String string) {
		return Static.Betrag(string);
	}

}
