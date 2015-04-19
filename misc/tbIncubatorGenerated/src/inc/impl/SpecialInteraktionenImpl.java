package inc.impl;

import inc.AutoSet;
import Neues_Schadensystem.Datentypen.Absicherung;
import Neues_Schadensystem.Datentypen.Schadenart;

public class SpecialInteraktionenImpl extends InteraktionenImpl{

	@AutoSet("Schadenart")
	private Schadenart schadenart;
	
	@AutoSet
	private Absicherung absicherung; 
	
	@Override
	public void LegeAn_Schaden() {
		System.out.println("LegeAn Schaden");
	}
	
	
}
