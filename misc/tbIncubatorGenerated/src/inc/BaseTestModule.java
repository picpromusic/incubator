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

import com.google.inject.AbstractModule;

public class BaseTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InteraktionenImpl.class).toProvider(
				new InteractionProxyProvider<InteraktionenImpl>(
						SpecialInteraktionenImpl.class));

		bind(TechnischImpl.class).toProvider(
				new InteractionProxyProvider<TechnischImpl>(
						SpecialTechnischImpl.class));

		bind(LdapUndCoImpl.class).toProvider(
				new InteractionProxyProvider<LdapUndCoImpl>(
						SpecialLdapUndCoImpl.class));

		bind(KundeInteraktionenImpl.class).toProvider(
				new InteractionProxyProvider<KundeInteraktionenImpl>(
						SpecialKundeInteraktionenImpl.class));

		bind(BestandImpl.class).toProvider(
				new InteractionProxyProvider<BestandImpl>(
						SpecialBestandImpl.class));

		bind(BusinessDelegateImpl.class).toProvider(
				new InteractionProxyProvider<BusinessDelegateImpl>(

						SpecialBusinessDelegateImpl.class));
		bind(PmsSzenarienImpl.class).toProvider(
				new InteractionProxyProvider<PmsSzenarienImpl>(
						SpecialPmsSzenarienImpl.class));

	}
}
