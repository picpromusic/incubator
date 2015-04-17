package inc.tf.ruleImpl;

import java.util.ArrayList;
import java.util.Collections;

import inc.tf.Interaction;
import inc.tf.InteractionManager;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SetImplementation implements TestRule {

	private final Class impl;

	public SetImplementation(Class impl) {
		this.impl = impl;
	}

	@Override
	public Statement apply(final Statement stmt, Description desc) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				Iterable<Class> interfaces = findAll(impl);

				if (!interfaces.iterator().hasNext()) {
					throw new RuntimeException("Konnte keine Interface in "
							+ impl
							+ " finden welches mit @Interaction annotiert ist");
				}

				for (Class interf : interfaces) {
					InteractionManager.put(interf, impl);
				}
				stmt.evaluate();
			}
		};
	}

	public static Iterable<Class> findAll(Class implementierung) {
		ArrayList<Class> interactionInterfaces = new ArrayList<Class>();
		Class implPointer = implementierung;
		while (!implPointer.equals(Object.class)) {
			Class[] interfaces = implPointer.getInterfaces();
			for (Class interf : interfaces) {
				if (interf.getAnnotation(Interaction.class) != null) {
					interactionInterfaces.add(interf);
				}
			}
			implPointer = implPointer.getSuperclass();
		}
		return Collections.unmodifiableCollection(interactionInterfaces);
	}
}
