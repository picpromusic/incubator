package tbIncubator.domain;

import java.util.ArrayList;
import java.util.Iterator;

import com.squareup.javapoet.ParameterSpec;

public class RepresentativeListBuildHelper {

	private TestSatz testSatz;

	public RepresentativeListBuildHelper(TestSatz testSatz) {
		this.testSatz = testSatz;
	}

	public Iterable<Representative> build() {
		final Iterator<Link> valueIt = testSatz.getParameterValues().iterator();

		return new Iterable<Representative>() {

			@Override
			public Iterator<Representative> iterator() {
				return new Iterator<Representative>() {

					private int num;

					{
						num = 1;
					}

					@Override
					public boolean hasNext() {
						return valueIt.hasNext();
					}

					@Override
					public Representative next() {
						ArrayList<Link> al = new ArrayList<Link>();
						for (InteractionParameter parameter : testSatz
								.getParameters()) {
							al.add(valueIt.next());
						}
						return new Representative("COMBI" + num++, null, al);
					}
				};
			}

		};
	}
}
