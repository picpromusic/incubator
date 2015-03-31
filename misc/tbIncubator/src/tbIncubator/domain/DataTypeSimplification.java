package tbIncubator.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import Neues_Schadensystem.Datentypen.Insuranceformat;

public enum DataTypeSimplification {
	Anzahl(new String[] { //
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Anzahl",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Laenge",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Objekt",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.LfdNr_Szenario",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Decimals",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Laenge",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Version",//
					"Neues_Schadensystem.Datentypen.Anzahl",//
			}, //
			"Anzahl", //
			Integer.class), //

	Text(new String[] { //
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Bezeichnung",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Formular",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Text",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.Langschrift",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.manueller_Text",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.Text_in_Oberflaeche",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Name",//
					"Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Tabellen_Name",//
					"Neues_Schadensystem.Datentypen.Abzugsnotiz",//
					"Neues_Schadensystem.Datentypen.Leistungsart_Ergaenzung",//
					"Neues_Schadensystem.Datentypen.Leistungsart_Meldungen",//
					"Neues_Schadensystem.Datentypen.Verwendungszweck",//
			},//
			"Text",//
			String.class), //
	Name(new String[] { //
			"Neues_Schadensystem.Datentypen.Name",//
			},//
			"Name",//
			String.class), //

	Wert(new String[] { //
			"Neues_Schadensystem.Datentypen.PMS_Datentypen.Wert",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.OB",//
					"Neues_Schadensystem.Datentypen.AKZ",//
					"Neues_Schadensystem.Datentypen.Bestand",//
					"Neues_Schadensystem.Datentypen.BIC",//
					"Neues_Schadensystem.Datentypen.FIN",//
					"Neues_Schadensystem.Datentypen.Hausnummer",//
					"Neues_Schadensystem.Datentypen.IBAN",//
					"Neues_Schadensystem.Datentypen.Kunde",//
					"Neues_Schadensystem.Datentypen.LfdNrAdr",//
					"Neues_Schadensystem.Datentypen.LfdNrBeziehung",//
					"Neues_Schadensystem.Datentypen.LfdNrKfz",//
					"Neues_Schadensystem.Datentypen.LfdNrKonto",//
					"Neues_Schadensystem.Datentypen.LfdNrSB",//
					"Neues_Schadensystem.Datentypen.Merkmalwert",//
					"Neues_Schadensystem.Datentypen.Nummer",//
					"Neues_Schadensystem.Datentypen.Ort",//
					"Neues_Schadensystem.Datentypen.PLZ",//
					"Neues_Schadensystem.Datentypen.Strasse",//
			},//
			"Wert",//
			String.class), //

	Schluessel(new String[] { //
			"Neues_Schadensystem.Datentypen.PMS_Datentypen.PBS",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.PRD",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Schluessel",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Version",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.VKE",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Schluessel",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Select_ID",//
					"Neues_Schadensystem.Datentypen.DECK_UMF_SCHL",//
					"Neues_Schadensystem.Datentypen.Deckungsart",//
					"Neues_Schadensystem.Datentypen.Exkasso_Sparte",//
					"Neues_Schadensystem.Datentypen.KF_TARIF_DIFF_KZ",//
					"Neues_Schadensystem.Datentypen.Merkmalschluessel",//
					"Neues_Schadensystem.Datentypen.Rolle",//
					"Neues_Schadensystem.Datentypen.Sparte",//
					"Neues_Schadensystem.Datentypen.VEP_Schluessel",//
					"Neues_Schadensystem.Datentypen.Vep",//
					"Neues_Schadensystem.Datentypen.WKZ",//
			},//
			"Schluessel",//
			String.class), //

	Datei(new String[] { //
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.CSV_Datei",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.PMS_Pfad",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.TOMAS_Tabellen_CSV",//
			},//
			"Datei",//
			String.class), //

	Betrag(new String[] { //
			"Neues_Schadensystem.Datentypen.Abrechnungsbetrag",//
					"Neues_Schadensystem.Datentypen.Betrag",//
					"Neues_Schadensystem.Datentypen.Reserve",//
			},//
			"Betrag",//
			BigDecimal.class), //

	Prozent(new String[] { //
			"Neues_Schadensystem.Datentypen.Prozent",//
			},//
			"Prozent",//
			BigDecimal.class), //

	Datum(new String[] { //
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.Datum",//
					"Neues_Schadensystem.Datentypen.PMS_Datentypen.TOMAS_Tabellen_Datum",//
					"Neues_Schadensystem.Datentypen.Datum",//
			},//
			"Datum",//
			Date.class), //
	;

	private String[] classNames;
	private String methodName;
	private Class valueType;
	private static final Map<String,DataTypeSimplification> INDEX;
	
	static {
		HashMap<String, DataTypeSimplification> temp = new HashMap<String, DataTypeSimplification>();
		for (DataTypeSimplification	ele : EnumSet.allOf(DataTypeSimplification.class)) {
			for (String className : ele.classNames) {
				temp.put(className, ele);
			}
		}
		INDEX = Collections.unmodifiableMap(temp);
	}
	
	private DataTypeSimplification(String[] classNames, String methodName,
			Class valueType) {
				this.classNames = classNames;
				this.methodName = methodName;
				this.valueType = valueType;
		
	}

	public DataTypeSimplification lookup(String className) {
		return INDEX.get(className);
	}
	
}
