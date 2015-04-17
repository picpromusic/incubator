package inc.tf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class InteractionManager {

	public interface Factory<T> {

		Class<T> getImplClass();

		T newInstance();

	}

	public static class ClassFactory<T> implements Factory<T> {

		private final Class<T> impl;

		public ClassFactory(Class<T> impl) {
			this.impl = impl;

		}

		@Override
		public Class<T> getImplClass() {
			return impl;
		}

		@Override
		public T newInstance() {
			try {
				return impl.getConstructor().newInstance();
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private final static ThreadLocal<InteractionManager> instance = new ThreadLocal<InteractionManager>();
	private Map<Class, Object> actualInstances;
	private Map<Class, Object> knownInstances;

	private InteractionManager() {
		this.actualInstances = new HashMap<Class, Object>();
		this.knownInstances = new HashMap<Class, Object>();
	}

	public static <T> T get(Class<T> clazz) {

		T object = (T) getInstance().actualInstances.get(clazz);
		if (object == null) {
			if (!clazz.isInterface()) {
				object = (T) getInstance().createInstance(
						new ClassFactory(clazz));
				getInstance().actualInstances.put(clazz, object);
			}
		}
		return object;
	}

	public static <T> void put(Class<? super T> clazz, Class<T> impl) {
		put(clazz, new ClassFactory<T>(impl));
	}

	public static <T> void put(Class<? super T> clazz, Factory<T> factory) {
		Object object = getInstance().actualInstances.get(clazz);
		if (object != null) {
			getInstance().knownInstances.put(object.getClass(), object);
		}
		object = getInstance().createInstance(factory);
		getInstance().actualInstances.put(clazz, object);
	}

	public static void cleanup() {
		getInstance().actualInstances.clear();
		getInstance().knownInstances.clear();
	}

	private <T> T createInstance(Factory<T> factory) {
		T object = (T) knownInstances.get(factory.getImplClass());
		if (object == null) {
			object = factory.newInstance();
		}
		return object;
	}

	private static InteractionManager getInstance() {
		InteractionManager interactionManager = instance.get();
		if (interactionManager == null) {
			interactionManager = new InteractionManager();
			instance.set(interactionManager);
		}
		return interactionManager;
	}

}
