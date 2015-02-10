package incubator;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExampleTest {

	private Teil t1;
	private Teil t2;
	private Teil t3;
	private Teil t4;

	@Before
	public void setup() {
		t1 = new Teil(11);
		t2 = new Teil(12);
		t3 = new Teil(32);
		t4 = new Teil(4711);

	}

	@Test
	public void testEinfach() {
		Zusammen z1 = new Zusammen();
		z1.add(t1, Ziele.A);
		z1.add(t2, Ziele.B);
		z1.add(t3, Ziele.C);

		Map<List<Teil>, EnumSet<Ziele>> erg = z1.getZielDateien();
		AssertEquals(3, erg.size());

		Assert.assertEquals(EnumSet.of(Ziele.A), erg.get(list(t1)));
		Assert.assertEquals(EnumSet.of(Ziele.B), erg.get(list(t2)));
		Assert.assertEquals(EnumSet.of(Ziele.C), erg.get(list(t3)));

		// Natürlich arbeitet man eher so damit. Fuer die Tests war das hier
		// aber eher hinderlich
		// Set<Entry<List<Teil>, EnumSet<Ziele>>> entrySet = erg.entrySet();
		// for (Entry<List<Teil>, EnumSet<Ziele>> entry : entrySet) {
		// PDF pdf = bauPDF(entry.getKey());
		// machWasMitZielen(pdf,entry.getValue());
		// }
	}

	@Test
	public void testKomplex() {
		Zusammen z1 = new Zusammen();
		z1.add(t1, Ziele.A);
		z1.add(t2, Ziele.B, Ziele.A);
		z1.add(t3, Ziele.C, Ziele.B);

		Map<List<Teil>, EnumSet<Ziele>> erg = z1.getZielDateien();
		AssertEquals(3, erg.size());

		Assert.assertEquals(EnumSet.of(Ziele.A), erg.get(list(t1, t2)));
		Assert.assertEquals(EnumSet.of(Ziele.B), erg.get(list(t2, t3)));
		Assert.assertEquals(EnumSet.of(Ziele.C), erg.get(list(t3)));
	}

	@Test
	public void testNochKomplexer() {
		Zusammen z1 = new Zusammen();
		z1.add(t1, Ziele.A);
		z1.add(t2, Ziele.B, Ziele.C);
		z1.add(t3, Ziele.B, Ziele.C);

		Map<List<Teil>, EnumSet<Ziele>> erg = z1.getZielDateien();
		AssertEquals(2, erg.size());

		Assert.assertEquals(EnumSet.of(Ziele.A), erg.get(list(t1)));
		Assert.assertEquals(EnumSet.of(Ziele.B, Ziele.C), erg.get(list(t2, t3)));
	}

	@Test
	public void testJetztIstAberGut() {
		Zusammen z1 = new Zusammen();
		z1.add(t1, Ziele.A);
		z1.add(t2, Ziele.B, Ziele.C);
		z1.add(t3, Ziele.B, Ziele.C);
		z1.add(t2, Ziele.B); // Absichtlich nochmal t2. Eigentlich wie
								// testNochKomplexer nur das man die Dateien für
								// B und C nicht zusammenfassen kann

		Map<List<Teil>, EnumSet<Ziele>> erg = z1.getZielDateien();
		AssertEquals(3, erg.size());

		Assert.assertEquals(EnumSet.of(Ziele.A), erg.get(list(t1)));
		Assert.assertEquals(EnumSet.of(Ziele.B), erg.get(list(t2, t3, t2)));
		Assert.assertEquals(EnumSet.of(Ziele.C), erg.get(list(t2, t3)));
	}

	private Object list(Teil... teile) {
		return Arrays.asList(teile);
	}

	private void AssertEquals(int size, int i) {
		// TODO Auto-generated method stub

	}
}