package inc;

import inc._Szenarien.IPmsSzenarien;

import java.util.Date;

import Neues_Schadensystem.Datentypen.Gesellschaft;
import Neues_Schadensystem.Datentypen.PMS_Datentypen.Datenformat;
import Neues_Schadensystem.Datentypen.PMS_Datentypen.Ja_Nein;

public class PmsSzenarienImpl implements IPmsSzenarien{

	@Override
	public void StelleSicher_AVB(String bedingung, String formular,
			String bezeichnung, String version_lang, String version_kurz,
			Date stand, String texte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_COA(String schluessel, String bezeichnung,
			Ja_Nein systemcodetabelle, Datenformat format, Integer laenge,
			Integer anzahl_der_EEG, Integer textlaenge_Langtext, String inhalt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_KBB(String klausel_BB, String bezeichnung,
			Date gueltig_ab, Date gueltig_bis, String langtext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_MEK(String schluessel, String bezeichnung,
			Date gueltig_ab, Date gueltig_bis,
			String schluessel_fuer_Codetabelle, String bezugstyp_Objekttypen,
			String bezugstyp_Objektarten, String format, String laenge,
			String minimum, String maximum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Leistungstyp(String produktbaustein,
			String cSV_Tabelle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_PBS(String produktbaustein,
			String bezeichnung_kurz, String bezeichnung_mittel,
			String bezeichnung_lang, Integer objekttyp, Integer objektart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Schadenart(String produktbaustein,
			String cSV_Tabelle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Schadenursache(String produktbaustein,
			String cSV_Tabelle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Bausteinzuordnung(String pRD, String pBS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_PRD(String produkt, String bezeichnung_kurz,
			String bezeichnung_mittel, String bezeichnung_lang) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Produktmerkmal(String produktmerkmal, Ja_Nein muﬂ,
			String berechnungs_Exit, String uMT, String pruefen_Exit,
			String defaultwert, String staffel, String wert_CSV_Tabelle,
			String klausel_oder_Selbstbehalt, String deckungsbedenken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Reserve(String reserveart, String bezeichnung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Schadenmerkmal(String schadenmerkmal, Ja_Nein muﬂ,
			String berechnungs_Exit, String uMT, String pruefen_Exit,
			String defaultwert, String staffel, String wert_CSV_Tabelle,
			String deckungsbedenken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Sparte(String schaden_Sparte, String bezeichnung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_UMT(String umschlTab, String bezeichnung,
			Date gueltig_ab, Date gueltig_bis, String merkmal_1,
			String bezeichnung_1, Ja_Nein staffel_Merkmal_1, String merkmal_2,
			String bezeichnung_2, Ja_Nein staffel_Merkmal_2, String merkmal_3,
			String bezeichnung_3, Ja_Nein staffel_Merkmal_3, String merkmal_4,
			String bezeichnung_4, Ja_Nein staffel_Merkmal_4, String cSV_Datei) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Gesellschaft(Gesellschaft gesellschaft,
			String bezeichnung) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Produktzuordnung(String vKE, String pRD) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_VKE(String vKE, String bezeichnung_kurz,
			String bezeichnung_mittel, String bezeichnung_lang) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_VKE_Merkmal(String vKE_Merkmal, Ja_Nein muﬂ,
			String berechnungs_Exit, String uMT, String pruefen_Exit,
			String defaultwert, String staffel, String wert_CSV_Tabelle,
			String klausel_oder_Selbstbehalt, String deckungsbedenken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StelleSicher_Zahlweise(String zahlweise, Ja_Nein ja_Nein,
			String bezeichnung, String klausel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Setze_Relativer_Pfad(String pMS_Pfad) {
		// TODO Auto-generated method stub
		
	}

}
