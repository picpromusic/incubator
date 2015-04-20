package inc;

import inc.impl.InteraktionenImpl;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;


public class BaseTestOberflaeche extends BasisAPI{

	private GuiceIntegration guice;

	@Inject 
	protected Szenarien Szenarien;
	
	@Inject
	protected InteraktionenImpl Interaktionen;


	public BaseTestOberflaeche() {
		this.guice = new GuiceIntegration();
	}
	
	@Before
	public void setup() {
		guice.setup(new BaseTestOberflaecheModule(),this);
	}

	
	@After
	public void tearDown() {
		guice.cleanup();
	}

}
