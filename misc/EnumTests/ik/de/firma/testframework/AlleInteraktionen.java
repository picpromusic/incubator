package de.firma.testframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AlleInteraktionen {

	public interface Destroyable {
		void destroy();
	}

	private static ThreadLocal<Map<Class<?>, Object>> map = new ThreadLocal<Map<Class<?>, Object>>();

	private static Collection<Object> get() {
		return ensureMap().values();
	}

	private static Map<Class<?>, Object> ensureMap() {
		if (map.get() == null) {
			map.set(new HashMap<Class<?>, Object>());
		}
		return map.get();
	}

	public static void destroyAll() {
		for (Object des : get()) {
			try {
				ensureMap().remove(des.getClass());
				if (des instanceof Destroyable) {
					((Destroyable) des).destroy();
				}
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		map.set(null);
	}

	public static <T> T self(Class<T> clazz) {
		try {
			Object instance = ensureMap().get(clazz);
			if (instance == null) {
				ensureMap().put(clazz, clazz.getConstructor().newInstance());
			}
			return (T) ensureMap().get(clazz);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
