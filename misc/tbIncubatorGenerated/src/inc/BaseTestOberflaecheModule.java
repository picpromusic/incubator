package inc;

import inc.impl.BestandImpl;
import inc.impl.BusinessDelegateImpl;
import inc.impl.InteraktionenImpl;
import inc.impl.KundeInteraktionenImpl;
import inc.impl.LdapUndCoImpl;
import inc.impl.SpecialBestandImpl;
import inc.impl.SpecialBusinessDelegateImpl;
import inc.impl.SpecialInteraktionenImpl;
import inc.impl.SpecialKundeInteraktionenImpl;
import inc.impl.SpecialLdapUndCoImpl;
import inc.impl.SpecialPmsSzenarienImpl;
import inc.impl.SpecialTechnischImpl;
import inc.impl.TechnischImpl;
import inc.impl.oberflaechen.AnspruchspositionAnlegenImpl;
import inc.impl.oberflaechen.SchadenAnlageImpl;
import inc.impl.oberflaechen.SchadenBearbeitenImpl;
import inc.impl.oberflaechen.SpecialAnspruchspositionAnlegenImpl;
import inc.impl.oberflaechen.SpecialSchadenAnlageImpl;
import inc.impl.oberflaechen.SpecialSchadenBearbeitenImpl;

import com.google.inject.AbstractModule;

public class BaseTestOberflaecheModule extends BaseTestModule {

	@Override
	protected void configure() {
		super.configure();

		bind(AnspruchspositionAnlegenImpl.class).toProvider(
				new InteractionProxyProvider<AnspruchspositionAnlegenImpl>(
						SpecialAnspruchspositionAnlegenImpl.class));

		bind(SchadenAnlageImpl.class).toProvider(
				new InteractionProxyProvider<SchadenAnlageImpl>(
						SpecialSchadenAnlageImpl.class));

		bind(SchadenBearbeitenImpl.class).toProvider(
				new InteractionProxyProvider<SchadenBearbeitenImpl>(
						SpecialSchadenBearbeitenImpl.class));

		
	}

}
