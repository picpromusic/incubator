package inc;

import inc.impl.InteraktionenImpl;

import javax.inject.Inject;

public class PMSBasisInteraktionen extends BasisAPI{

	@Inject
	protected PmsSzenarienImpl PmsSzenarien;
	
	@Inject
	protected InteraktionenImpl Interaktionen;


}
