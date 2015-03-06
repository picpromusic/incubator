package test.de.firma.projekt.technisch.datentypen;

public enum Position {

	GUELTIG;

	public Integer genau(int i) {
		if (this == GUELTIG) {
			return new Integer(i);
		}
		return null;
	}
}