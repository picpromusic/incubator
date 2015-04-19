package inc.tf.ruleImpl;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class FinallyRule implements TestRule {

	public interface Runner {
		public void run();
	}

	private Runner fr;

	public FinallyRule(Runner fr) {
		this.fr = fr;
	}

	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				} finally {
					fr.run();
				}
			}

		};
	}

}
