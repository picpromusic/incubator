package inc.tf.ruleImpl;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestDataRule implements TestRule {

	@Override
	public Statement apply(final Statement arg0, final Description arg1) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				
			}
		};
		// return arg0;
	}

}
