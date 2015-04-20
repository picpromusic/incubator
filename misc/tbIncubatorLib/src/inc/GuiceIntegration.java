package inc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceIntegration {

	private Injector injector;

	public void setup(Module module, Object injectInto) {
		this.injector = Guice.createInjector(module);
		this.injector.injectMembers(injectInto);
	}

	public void cleanup() {
		this.injector = null;
	}

}
