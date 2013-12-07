package lambdaActors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DefineBehaviorSentence<T> {

	private T impl;

	public void addImplementation(T object) {
		this.impl = object;
	}

	public Object call() throws IllegalAccessException,
			InvocationTargetException, IllegalArgumentException {
		Method[] declaredMethods = impl.getClass().getDeclaredMethods();
		return declaredMethods[0].invoke(impl, null);
	}
}
