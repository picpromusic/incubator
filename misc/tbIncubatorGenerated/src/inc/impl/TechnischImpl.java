package inc.impl;

import inc.ITechnisch;
import inc.tf.InteractionManager;
import inc.tf.ruleImpl.SetImplementation;
import Technisch.Interaktionskontext;

public class TechnischImpl implements ITechnisch {

	@Override
	public void Ende_Scenario(Integer lfdNr_Szenario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Initiale_Datenbank() {

	}

	@Override
	public void Setze_Interaktionskontext(
			Interaktionskontext interaktionskontext) {
		Iterable<Class> interfaces = SetImplementation
				.findAll(interaktionskontext.implementierung);
		for (Class interf : interfaces) {
			InteractionManager.put(interf, interaktionskontext.implementierung);
		}
	}

	@Override
	public void Start_Scenario(Integer lfdNr_Szenario) {
		// TODO Auto-generated method stub

	}

}
