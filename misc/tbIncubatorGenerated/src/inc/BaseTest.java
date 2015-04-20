package inc;

import inc.impl.InteraktionenImpl;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;

public abstract class BaseTest extends BasisAPI {

	protected final GuiceIntegration guice;
	
	@Inject
	protected Szenarien Szenarien;
	
	@Inject
	protected InteraktionenImpl Interaktionen;

	
	
	public BaseTest() {
		this.guice = new GuiceIntegration();
	}
	
	@Before
	public void setup() {
		guice.setup(new BaseTestModule(),this);
	}

	
	@After
	public void tearDown() {
		guice.cleanup();
	}
	
	
}
