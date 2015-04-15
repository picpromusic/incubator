package inc;

import java.util.EnumSet;
import java.util.Iterator;

import org.junit.runners.Parameterized.Parameters;

import Neues_Schadensystem.Schaden_anlegen.Backend.Eingabe_Tote_und_Verletze_ermoeglichen.Data;

public class BaseTestWithData<DATA> extends BaseTest {

	protected DATA parameter;

	public BaseTestWithData(DATA parameter) {
		this.parameter = parameter;
	}

	@Parameters
	public static Iterable<Object[]> parameters() {
		return new Iterable<Object[]>() {

			@Override
			public Iterator<Object[]> iterator() {
				EnumSet allOf = EnumSet
						.allOf((Class<? extends Enum>) Data.class);
				final Iterator iterator = allOf.iterator();
				return new Iterator<Object[]>() {

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public Object[] next() {
						return new Object[] { iterator.next() };
					}

					@Override
					public void remove() {
						throw new RuntimeException("Not supported");
					}
				};
			}
		};
	}

}
