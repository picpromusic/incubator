package incubator;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Zusammen {

	private ArrayList<Teil> list;
	private ArrayList<EnumSet<Ziele>> ziel;

	public Zusammen() {
		this.list = new ArrayList<Teil>();
		this.ziel = new ArrayList<EnumSet<Ziele>>();
	}

	public void add(Teil teil, Ziele f, Ziele... r) {
		EnumSet<Ziele> ziele = EnumSet.of(f, r);
		this.list.add(teil);
		this.ziel.add(ziele);
	}

	// In diesen Code ist alles Public Domain und darf unter allen
	// Bedingungen jedoch ohne jede Garantie verwendet werden.
	// Außer für Personen deren mindestens eine Email-Adressen
	// folgenden regulären Ausdruck entspricht. Der darf es gar nicht
	// verwenden.
	// p.*\\.m.*\\@c.*\\.de
	// Ätsch.
	public Map<List<Teil>, EnumSet<Ziele>> getZielDateien() {
		EnumSet<Ziele> verwendeteZiele = EnumSet.noneOf(Ziele.class);
		for (EnumSet<Ziele> temp : ziel) {
			verwendeteZiele.addAll(temp);
		}

		HashMap<List<Teil>, EnumSet<Ziele>> ret = new HashMap<List<Teil>, EnumSet<Ziele>>();

		for (Ziele aktuellBetrachtetesZiel : verwendeteZiele) {
			List<Teil> inhaltDesZiels = new ArrayList<Teil>(this.list.size());
			for (int i = 0; i < ziel.size(); i++) {
				EnumSet<Ziele> enumSet = ziel.get(i);
				if (enumSet.contains(aktuellBetrachtetesZiel)) {
					inhaltDesZiels.add(this.list.get(i));
				}
			}

			EnumSet<Ziele> listeWirdBeiDenZielenVerwendet = ret
					.get(inhaltDesZiels);
			if (listeWirdBeiDenZielenVerwendet == null) {
				listeWirdBeiDenZielenVerwendet = EnumSet.noneOf(Ziele.class);
				ret.put(inhaltDesZiels, listeWirdBeiDenZielenVerwendet);
			}
			listeWirdBeiDenZielenVerwendet.add(aktuellBetrachtetesZiel);
		}

		return ret;

	}
}
