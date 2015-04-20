package inc.impl.oberflaechen;

import inc.IOberflaeche;

import java.math.BigDecimal;
import java.util.Date;

import Neues_Schadensystem.Datentypen.Abrechnungstyp;
import Neues_Schadensystem.Datentypen.Absicherung;
import Neues_Schadensystem.Datentypen.Abwicklung;
import Neues_Schadensystem.Datentypen.Brutto_Netto_Vst_Kennzeichen;
import Neues_Schadensystem.Datentypen.Gesellschaft;
import Neues_Schadensystem.Datentypen.Leistungsart;
import Neues_Schadensystem.Datentypen.Meldungen;
import Neues_Schadensystem.Datentypen.OE;
import Neues_Schadensystem.Datentypen.Ordnungsbegriff;
import Neues_Schadensystem.Datentypen.Pruefvermerk;
import Neues_Schadensystem.Datentypen.Relevant;
import Neues_Schadensystem.Datentypen.Risiko_Objekt;
import Neues_Schadensystem.Datentypen.SFR_belastend;
import Neues_Schadensystem.Datentypen.Sachbearbeiter;
import Neues_Schadensystem.Datentypen.Schadenart;
import Neues_Schadensystem.Datentypen.Status;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.GUI_Status;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.Select_ID;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.Wahrheitswert;

public abstract class AbstractOberflaechenImpl implements IOberflaeche{

	@Override
	public void Breche_AP_Anlage_ab() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Abrechnungstyp(Abrechnungstyp abrechnungstyp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Abzug(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Bezeichnung_des_Abzuges(String abzugsnotiz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Leistungsart(Leistungsart leistungsart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Leistungsarten_Ergaenzung(String leistungsart_Ergaenzung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Pruefvermerk(Pruefvermerk pruefvermerk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Relvant_Vermerk(Relevant relevant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Steuermerkmal(
			Brutto_Netto_Vst_Kennzeichen brutto_Netto_Vst_Kennzeichen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Verwendungszweck(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Zahlungsempfaenger(String kunde, String rolle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Zahlungsempfaenger_hinzufuegen(GUI_Status gUI_Status,
			Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_geforderten_Betrag(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungstyp(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungstyp_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungstyp_Moeglichkeit(Select_ID select_ID,
			Abrechnungstyp abrechnungstyp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungsweg_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungsweg_Moeglichkeit(Select_ID select_ID,
			String lfdNrKonto, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungsweg_Wert(String lfdNrKonto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abrechnungweg(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abzug(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abzug_Betrag(GUI_Status gUI_Status,
			BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Bezeichnung_des_Abzuges(GUI_Status gUI_Status,
			String manueller_Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Brutto(Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Entschaedigung(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Entschaedigung_Betrag(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Leistungsart(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Leistungsart_Ergaenzung(GUI_Status gUI_Status,
			String manueller_Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Leistungsart_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Leistungsart_Moeglichkeit(Select_ID select_ID,
			Leistungsart leistungsart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Netto(Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Pruefvermerk(Pruefvermerk pruefvermerk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Relevanzvermerk(Pruefvermerk pruefvermerk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Verwendungszweck(GUI_Status gUI_Status,
			String manueller_Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Vst_nicht_geklaert(Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Zahlungsempfaenger(GUI_Status gUI_Status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Zahlungsempfaenger_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Zahlungsempfaenger_Moeglichkeit(
			Select_ID select_ID, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Zahlungsempfaenger_hinzufuegen(
			GUI_Status gUI_Status, Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_geforderter_Betrag(GUI_Status gUI_Status,
			BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_geforderter_Betrag_Wert(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Breche_Schadenanlage_ab() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Erstelle_Schaden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Absicherung(Absicherung absicherung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Erstkontaktdatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Reserve(BigDecimal reserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_SFR_Belastend(SFR_belastend sFR_belastend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Sachbearbeiter_Name(Sachbearbeiter sachbearbeiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Sachbearbeiter_OE(OE oE) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Schadenart(Schadenart schadenart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Schadendatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Tote(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Verletzte(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Schadenanlage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Schadengrunddaten(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Absicherung(GUI_Status gUI_Status,
			Absicherung absicherung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Absicherung_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Absicherung_Moeglichkeit(Integer position,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Erstkontaktdatum(GUI_Status gUI_Status, Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Gesellschaft(Gesellschaft gesellschaft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Meldung(Meldungen meldungen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Reserve(GUI_Status gUI_Status, BigDecimal reserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_1_WKZ(Select_ID select_ID,
			String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_1_Aktiv(GUI_Status gUI_Status,
			Select_ID select_ID, String text_in_Oberflaeche, String langschrift) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_1_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_1_Moeglichkeiten(Integer position,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_2_AKZ_FIN(Select_ID select_ID,
			String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_2_Aktiv(GUI_Status gUI_Status,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_2_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_2_Moeglichkeiten(Integer position,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_3_Aktiv(GUI_Status gUI_Status,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_3_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Risiko_Ebene_3_Moeglichkeiten(
			GUI_Status gUI_Status, Select_ID select_ID,
			String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_SFR_Belastend(GUI_Status gUI_Status,
			Wahrheitswert wahrheitswert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_SFR_Datum(GUI_Status gUI_Status, Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_Moeglichkeit(Integer position,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_Name(GUI_Status gUI_Status,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_Name_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_OE(GUI_Status gUI_Status,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_OE_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sachbearbeiter_OE_Moeglichkeit(Integer position,
			Select_ID select_ID, String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadenanlagedatum(GUI_Status gUI_Status, Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadenart(GUI_Status gUI_Status,
			Schadenart schadenart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadenart_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadenart_Moeglichkeit(Select_ID select_ID,
			String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadendatum(GUI_Status gUI_Status, Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadennummer(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Schadenstatus(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Sparte(String sparte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Tote(GUI_Status gUI_Status, Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Verletzte(GUI_Status gUI_Status, Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Abwicklung(Abwicklung abwicklung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Anspruchspositionen_Anzahl(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Kontostand(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_VEP(String vEP_Schluessel, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_VN_Bezeichnung(String text_in_Oberflaeche) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Vollmacht(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_RO(Risiko_Objekt risiko_Objekt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Vertrag(String bestand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Vorbereiten_Anspruchsposition_anlegen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Vorbereiten_Schaden_Anlage_Normalschaden(
			Ordnungsbegriff ordnungsbegriff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_KFZ_Kopf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Spartenuebergreifender_Kopf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_AKZ_FIN(String aKZ, String fIN, Integer position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Anzahl_AKZ_FIN(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_Anzahl_WKZ(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_RO(Risiko_Objekt risiko_Objekt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_SFR_Belastung(SFR_belastend sFR_belastend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ueberpruefe_WKZ(String wKZ, Integer position) {
		// TODO Auto-generated method stub
		
	}

}
