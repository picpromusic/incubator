package inc;

import static inc.Releases.*;

public enum Kanban {

	K1(HR1_2015), K2(HR1_2015), K3(HR2_2015), K4(HR3_2015);

	private final Releases released;
	private final boolean enabled;

	private Kanban(Releases toBeReleasedIn) {
		this.released = toBeReleasedIn;
		this.enabled = Boolean.getBoolean("ENABLE_" + this.name())
				|| this.released.isEnabled();
	}
	
	

}
