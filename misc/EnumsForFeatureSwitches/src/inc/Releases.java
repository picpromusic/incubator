package inc;

import java.util.EnumSet;
import static inc.Environment.*;

public enum Releases {

	HR1_2015(PRODUCTION), //
	HR2_2015(TEST_UND_ENTWICKLUNG), //
	HR3_2015(TEST_UND_ENTWICKLUNG), //
	TO_BE_ANNOUNCED(TEST_UND_ENTWICKLUNG);

	private static final int MAX = 4;
	private final boolean enabled;

	static {
		EnumSet<Releases> all = EnumSet.allOf(Releases.class);
		if (all.size() > MAX) {
			throw new RuntimeException("Zu viele Releases. Erlaubt sind " + MAX
					+ " " + all);
		}
	}

	private Releases(Environment enviro) {
		this.enabled = Boolean.getBoolean("ENABLE_" + this.name())
				|| enviro == PRODUCTION;
	}

	public boolean isEnabled() {
		return enabled;
	}
}