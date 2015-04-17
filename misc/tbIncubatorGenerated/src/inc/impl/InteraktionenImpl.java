package inc.impl;

import inc.allgemein.IInteraktionen;

import java.math.BigDecimal;
import java.util.Date;

import Neues_Schadensystem.Datentypen.Abrechnungsart;
import Neues_Schadensystem.Datentypen.Abrechnungstyp;
import Neues_Schadensystem.Datentypen.Absicherung;
import Neues_Schadensystem.Datentypen.Brutto_Netto_Vst_Kennzeichen;
import Neues_Schadensystem.Datentypen.Deckungsbedenken;
import Neues_Schadensystem.Datentypen.Gesellschaft;
import Neues_Schadensystem.Datentypen.Insuranceformat;
import Neues_Schadensystem.Datentypen.Leistungsart;
import Neues_Schadensystem.Datentypen.Meldungen;
import Neues_Schadensystem.Datentypen.MeldungsFeldReferenz;
import Neues_Schadensystem.Datentypen.Merkmalname;
import Neues_Schadensystem.Datentypen.OE;
import Neues_Schadensystem.Datentypen.Pruefvermerk;
import Neues_Schadensystem.Datentypen.Relevant;
import Neues_Schadensystem.Datentypen.SFR_belastend;
import Neues_Schadensystem.Datentypen.Sachbearbeiter;
import Neues_Schadensystem.Datentypen.Schadenart;
import Neues_Schadensystem.Datentypen.Status;
import Neues_Schadensystem.Datentypen.versichert_und_nicht_versichert;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Datentyp;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Primaerschluessel;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Spalten_Vorzeichen;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Tabellen_CSV;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Tabellen_Datum;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.TOMAS_Tabellen_Version;
import Neues_Schadensystem.Datentypen.Technische_Datentypen.Wahrheitswert;

public class InteraktionenImpl implements IInteraktionen{

	@Override
	public void Checke_AKZ(String aKZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_relevant(Pruefvermerk pruefvermerk, String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_relevant_Vorbelegung(Relevant jA_oder_Nein) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_Inhaber(String lfdNrBeziehung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_Inhaber_Vorbelegung(String lfdNrBeziehung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_Nummer(String nummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_Pruefvermerk(Pruefvermerk pruefvermerk,
			String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_AP_Status(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungsart(Abrechnungsart abrechnungsart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungsartenliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungsartenliste_Listeneintrag(
			Abrechnungsart abrechnungsart, Integer listeneintrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungstyp(Abrechnungstyp abrechnungstyp,
			String typschluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungstyp_Vorbelegung(Abrechnungstyp abrechnungstyp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungstypenliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abrechnungstypenliste_Listeneintrag(
			Abrechnungstyp abrechnungstyp, Integer listeneintrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Absicherung(Absicherung absicherung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Absicherungsliste(Integer listenlaenge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Absicherungsliste_Listeneintrag(Absicherung absicherung,
			Integer listenposition,
			versichert_und_nicht_versichert versichert_und_nicht_versichert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abzug(BigDecimal abzug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abzug_Bezeichnung(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Abzugsnotiz(String abzugsnotiz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Adresse(String kunde, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Adresse_Vorbelegung(String kunde, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Anspruchspositionen(
			Integer anzahl_Anspruchspositionen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Deckungsbedenken(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Fahrzeugdatenvorbelegung(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Meldungen(Integer anzahl_Anspruchspositionen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Schadenbeziehung(Integer anzahl_Schadenbeziehungen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Schaeden(Integer anzahl_Schaeden) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_Zahlungen(Integer anzahl_Buchungen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Anzahl_relevante_Selbstbeteiligung(
			Integer anzahl_vorhandene_Selbstbeteiligungen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Beteiligung_an_Zahlungen(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Betrag_Selbstbeteiligung(BigDecimal selbstbeteilgung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Betrag_gefordert(BigDecimal betrag_Anspruchsposition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Brutto_Netto_Vst_Kennzeichen(
			Brutto_Netto_Vst_Kennzeichen brutto_Netto_Vst_Kennzeichen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Buchungsdatum(Date buchungsdatum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Buchungsdatumvorbelegung(Date buchungsdatum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_Obliegenheiten_nach_Schadeneintritt(
			Deckungsbedenken deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_Obliegenheiten_nach_Schadeneintritt_Vorbelegung(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_Obliegenheiten_vor_Schadeneintritt(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_Obliegenheiten_vor_Schadeneintritt_Vorbelegung(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_sonstige_man_Deckung_Vorbelegung(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckung_vorhanden_zu_sonstige_manuelle_Deckung(
			Deckungsbedenken deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckungsbedenken(Deckungsbedenken deckungsbedenken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckungsbedenken_schadenschlieﬂungsverhindernd(
			Relevant ja_oder_Nein) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Deckungsbedenken_zahlungsverhindernd(
			Relevant ja_oder_Nein) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Eingabe_Tote_Verletzte_moeglich(
			Wahrheitswert eingabeMoeglich) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Entschaedigungsbetrag(BigDecimal abrechnungsbetrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Erstkontaktdatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Exkasso_Sparte(String exkasso_Sparte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_FIN(String fIN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Fahrzeugdatenbelegung(String wKZ, String aKZ, String fIN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Fahrzeugdatenvorbelegung(String wKZ, String aKZ,
			String fIN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Forderung_aus_Selbstbeteiligung(
			BigDecimal selbstbeteiligung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Gesamt_Entschaedigung(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Gesamt_Rest_SB(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Gesamt_Saldo(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Gesamt_reguliert(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Gesellschaft(Gesellschaft gesellschaft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Hoehe_Deckung_maschinell(BigDecimal maschinelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Kontoverbindung(String kunde, String persKoVKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Kontoverbindung_Vorbelegung(String kunde,
			String lfdNrKonto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Kontoverbindungen_Listeneintrag(String kunde,
			String persKoVKey, Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Kontoverbindungsliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Kundennummer(String kunde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Leistungsart(Leistungsart leistungsart, String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Leistungsart_Ergaenzung(String leistungsart_Ergaenzung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Leistungsartenliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Leistungsartenliste_Listeneintrag(
			Leistungsart leistungsart, Integer listenposition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Leistungsartenvorbelegung(Leistungsart leistungsart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Meldung(Meldungen fehlermeldungen,
			MeldungsFeldReferenz meldungsFeldReferenz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_OE(OE oE) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_OE_Liste(Integer oE_n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_OE_Liste_Listeneintrag(OE oE, Integer listenposition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Pruefvermerk_Vorbelegung(Pruefvermerk pruefvermerk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Relevanzvermerk_Vorbelegung(Pruefvermerk pruefvermerk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Reserve(BigDecimal reserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Reservevorbelegung(BigDecimal reserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Restbetrag_Selbstbeteiligung(BigDecimal selbstbeteiligung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Risikoliste(Integer listenlaenge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Risikoliste_Listeneintrag(String aKZ, String fIN,
			Integer listeneintrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Rollenbezeichnung(String rolle,
			String abweichendInPartner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Rollenliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Rollenliste_Listeneintrag(String rolle,
			Integer listeneintrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_SB_Konto(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_SFR_Vorbelegung(SFR_belastend sFR_belastend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_SFR_belastend(SFR_belastend sFR_belastend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Sachbearbeiterliste(Integer mitarbeiteranzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Sachbearbeiterliste_Listeneintrag(
			Sachbearbeiter sachbearbeiter, Integer listenposition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Saldo(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenanlagedatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenart(Schadenart schadenart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenartenliste(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenartenliste_Listeneintrag(Schadenart schadenart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenmerkmal(Merkmalname generischer_Parameter,
			String generischer_Parameter_1,
			Insuranceformat generischer_Parameter_2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadennummer(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenschlieﬂungsdatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadenstatus(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Schadentag(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Scheckempfaenger(String kunde, String persAdrKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Sparte(String sparte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Stamm_Sachbearbeiter(Sachbearbeiter sachbearbeiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Tote(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Verletzte(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Vertragsnummer(String bestand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Verwendungszeck_gesamt(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Verwendungszweck(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Verwendungszweck_Vorbelegung(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Vorbelegung_Tote(Integer anzahl_Tote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Vorbelegung_Verletzte(Integer anzahl_Verletzte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Vorbelegung_zu_beruecksichtigende_Forderung_aus_SB(
			BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Vorbelegung_zu_beruecksichtigende_Selbstbeteiligung(
			BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_WKZ(String wKZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_WKZ_Liste(Integer listenlaenge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_WKZ_Liste_Listeneintrag(String wKZ, Integer listeneintrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsbetrag(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsbetrag_Vorbelegung(BigDecimal zahlungsbetrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsempfaenger(String rolle, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsempfaengerliste(
			Integer anzahl_der_moegliche_Zahlungsempfaenger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsempfaengerliste_Listeneintrag(String kunde,
			String persAdrKey, String rolle, Integer position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsnummer(String nummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_Zahlungsstatus(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Checke_auf_Meldungen_beim_Schaden_schlieﬂen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ermittle_Sachbearbeiterliste() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Erstelle_Anspruchsposition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Erstelle_Zahlung_auf_gewuenschte_Anspruchsposition_en() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LegeAn_Anspruchsposition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LegeAn_Schaden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LegeAn_Schadenbeziehung() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LegeAn_Zahlung() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Schlieﬂe_Schaden(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_AKZ(String aKZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_AKZ_FIN(String aKZ, String fIN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_AP_relevant(Pruefvermerk pruefvermerk, String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_AP_Inhaber(String lfdNrBeziehung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_AP_Pruefvermerk(Pruefvermerk pruefvermerk,
			String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Abrechnungsart(Abrechnungsart abrechnungsart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Abrechnungsbetrag(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Abrechnungstyp(Abrechnungstyp abrechnungstyp,
			String typschluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Absicherung(Absicherung absicherung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Abzug(BigDecimal abzug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Abzugsnotiz(String abzugsnotiz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Adresse(String kunde, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Betrag_gefordert(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Brutto_Netto_Vst_Kennzeichen(
			Brutto_Netto_Vst_Kennzeichen brutto_Netto_Vst_Kennzeichen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Deckung_vorhanden_zu_Obliegenheiten_nach_Schadeneintritt(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Deckung_vorhanden_zu_Obliegenheiten_vor_Schadeneintritt(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Deckung_vorhanden_zu_sonstige_manuelle_Deckung(
			Deckungsbedenken manuelle_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Deckungsbedenken(Deckungsbedenken deckungsbedenken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Erstkontaktdatum(String datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_FIN(String fIN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Hoehe_Deckung(BigDecimal hoehe_der_Deckung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Kontoverbindung(String kunde, String persKoVKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Leistungsart(Leistungsart leistungsart, String schluessel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Leistungsart_Ergaenzung(String leistungsart_Ergaenzung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_LfdNrKfz(String lfdNrKfz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_OE(OE oE) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Reserve(BigDecimal reserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Rollenbezeichnung(String rolle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_SFR_belastend(SFR_belastend sFR_belastend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_SFR_belastend_Datum(Date sFR_Belastend_Datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Schadenart(Schadenart schadenart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Schadenbeziehung(String schadennummer,
			String lfdNrBeziehung, String kunde, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Schadenmerkmal(Merkmalname schluessel_zum_Merkmal,
			String wert_des_Merkmals, Insuranceformat format_in_Insurance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Schadennummer(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Schadentag(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Scheckempfaenger(String kunde, String persAdrKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Sparte(String sparte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Stamm_Sachbearbeiter(Sachbearbeiter sachbearbeiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Tote(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_VS_Nr(String bestand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Verletzte(Integer anzahl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Verwendungszweck(String verwendungszweck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_WKZ(String wKZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Zahlungsempfaenger(String rolle, String lfdNrAdr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_geforderten_Betrag(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_zu_beruecksichtigende_Forderung_aus_SB(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_zu_beruecksichtigende_Selbstbeteiligung(BigDecimal betrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_BestandsVep(String vEP_Schluessel, String name,
			String bestand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Rolle(String rolle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Ruhebeginn(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Ruheende(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_TOMAS_Tabelle(String tOMAS_Tabellen_Name,
			TOMAS_Tabellen_Version tOMAS_Tabellen_Version,
			TOMAS_Tabellen_Datum gueltig_ab, TOMAS_Tabellen_Datum gueltig_bis,
			TOMAS_Tabellen_CSV cSV_Datei, TOMAS_Tabellen_Datum bAT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_TOMAS_Tabelle_Spalte(String tOMAS_Spalten_Name,
			TOMAS_Spalten_Primaerschluessel tOMAS_Spalten_Primaerschluessel,
			TOMAS_Spalten_Datentyp tOMAS_Spalten_Datentyp,
			Integer tOMAS_Spalten_Laenge, Integer tOMAS_Spalten_Decimals,
			TOMAS_Spalten_Vorzeichen tOMAS_Spalten_Vorzeichen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Tagesdatum(Date datum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_angemeldeter_User(Sachbearbeiter user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Anspruchsposition(String lfdNrAP) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Waehle_Risiko() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Aendere_Schadendaten() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Anspruchsposition(String lfdNrAP) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Anspruchsposition_Vorbelegung() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Deckungsbedenken() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Schaden(String schadennummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Schadenbeziehung(String lfdNrBeziehung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Zahlung(String buchungsnummer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_Zahlungsvorbelegung() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Oeffne_relevante_Selbstbeteiligung(String lfdNrSB) {
		// TODO Auto-generated method stub
		
	}

}
