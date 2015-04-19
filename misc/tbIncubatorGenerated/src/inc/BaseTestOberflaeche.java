package inc;

import inc.tf.ruleImpl.FinallyRule;
import inc.tf.ruleImpl.FinallyRule.Runner;

import org.junit.Rule;
import org.junit.rules.TestRule;

public class BaseTestOberflaeche extends ZusammengesetzteInteraktionOberflaeche {

	@Rule
	public TestRule setImpl = new FinallyRule(new Runner() {
		@Override
		public void run() {
			instanziiereNeueProxies();
		}
	});

}
