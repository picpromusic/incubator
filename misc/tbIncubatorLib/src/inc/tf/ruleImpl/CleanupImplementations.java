package inc.tf.ruleImpl;

import inc.tf.InteractionManager;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class CleanupImplementations implements TestRule {

	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				} finally {
					InteractionManager.cleanup();
				}
			}

		};
	}

}
