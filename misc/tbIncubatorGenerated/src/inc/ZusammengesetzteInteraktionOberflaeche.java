package inc;

import inc.impl.oberflaechen.AnspruchspositionAnlegenImpl;
import inc.impl.oberflaechen.SchadenAnlageImpl;
import inc.impl.oberflaechen.SchadenBearbeitenImpl;
import inc.impl.oberflaechen.SpecialAnspruchspositionAnlegenImpl;
import inc.impl.oberflaechen.SpecialSchadenAnlageImpl;
import inc.impl.oberflaechen.SpecialSchadenBearbeitenImpl;

import java.lang.reflect.Field;

public class ZusammengesetzteInteraktionOberflaeche extends ZusammengesetzteInteraktion{

	@Impl(SpecialAnspruchspositionAnlegenImpl.class)
	protected static AnspruchspositionAnlegenImpl AnspruchspositionAnlegen;

	@Impl(SpecialSchadenAnlageImpl.class)
	protected static SchadenAnlageImpl SchadenAnlage;

	@Impl(SpecialSchadenBearbeitenImpl.class)
	protected static SchadenBearbeitenImpl SchadenBearbeiten;
	
	static {
		instanziiereNeueProxies();
	}

	protected static void instanziiereNeueProxies() {
		for (Field field : ZusammengesetzteInteraktionOberflaeche.class
				.getDeclaredFields()) {
			Impl annotation = field.getAnnotation(Impl.class);
			if (annotation != null) {
				try {
					field.set(null, InteractionProxy.build(annotation.value()));
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		ZusammengesetzteInteraktion.instanziiereNeueProxies();
	}
}
