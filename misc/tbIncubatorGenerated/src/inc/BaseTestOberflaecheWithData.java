package inc;

import java.util.EnumSet;
import java.util.Iterator;

import org.junit.runners.Parameterized.Parameters;

public class BaseTestOberflaecheWithData<DATA> extends BaseTestOberflaeche {

	protected DATA parameter;

	public BaseTestOberflaecheWithData(DATA parameter) {
		this.parameter = parameter;
	}

	@Parameters(name="{0})")
	protected static Iterable<Object[]> parametersHelper(final Class clazz) {
		return new Iterable<Object[]>() {

			@Override
			public Iterator<Object[]> iterator() {
				EnumSet allOf = EnumSet
						.allOf((Class<? extends Enum>) clazz);
				final Iterator iterator = allOf.iterator();
				return new Iterator<Object[]>() {

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public Object[] next() {
						Object next = iterator.next();
						return new Object[] { next };
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
