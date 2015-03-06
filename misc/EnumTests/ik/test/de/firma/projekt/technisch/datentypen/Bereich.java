package test.de.firma.projekt.technisch.datentypen;

public interface Bereich {

	public static Bereich genau(int i) {
		return new Genau(i);
	}
	
	public int von();
	public int bis();
}
