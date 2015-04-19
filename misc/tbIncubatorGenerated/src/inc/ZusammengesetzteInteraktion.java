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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class ZusammengesetzteInteraktion {

	@Impl(SpecialTechnischImpl.class)
	protected static TechnischImpl Technisch;

	@Impl(SpecialLdapUndCoImpl.class)
	protected static LdapUndCoImpl LdapUndCoInteraktionen;

	@Impl(SpecialKundeInteraktionenImpl.class)
	protected static KundeInteraktionenImpl KundeInteraktionen;

	@Impl(SpecialInteraktionenImpl.class)
	protected static InteraktionenImpl Interaktionen;

	@Impl(SpecialBestandImpl.class)
	protected static BestandImpl BestandInteraktionen;

	@Impl(SpecialBusinessDelegateImpl.class)
	protected static BusinessDelegateImpl BusinessDelegateInteraktionen;

	@Impl(SpecialPmsSzenarienImpl.class)
	protected static PmsSzenarienImpl PmsSzenarien;

	static {
		instanziiereNeueProxies();
	}

	protected static void instanziiereNeueProxies() {
		for (Field field : ZusammengesetzteInteraktion.class
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
	}

	protected static Date Datum(String string) {
		return Static.Datum(string);
	}

	protected static Integer Anzahl(String string) {
		return Static.Anzahl(string);
	}

	protected static BigDecimal Betrag(String string) {
		return Static.Betrag(string);
	}

}
