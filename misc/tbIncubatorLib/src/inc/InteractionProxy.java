package inc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class InteractionProxy {

	private final static Map<Class, Map<String, Field>> autoSetCacheMaps = new HashMap<Class, Map<String, Field>>();

	public static <T> T build(final Class<? extends T> impl) {
		Enhancer enhancer = new Enhancer();
		enhancer.setClassLoader(impl.getClassLoader());
		enhancer.setSuperclass(impl);
		enhancer.setCallback(new MethodInterceptor() {

			@Override
			public Object intercept(Object obj, Method method, Object[] args,
					MethodProxy mProxy) throws Throwable {
				Class<?> implPointer = impl;
				String methodName = method.getName();
				String fieldName = null;
				if (args.length == 1) {
					int length = 0;
					if (methodName.startsWith("Setze")) {
						length = "Setze".length();
					} else if (methodName.startsWith("Stelle_Sicher")) {
						length = "Stelle_Sicher".length();
					}
					if (length > 0) {
						fieldName = methodName.substring(length);
						fieldName = fieldName.replaceAll("_", " ").trim()
								.toLowerCase();
					}
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				do {
					Method implMethod = implPointer.getMethod(methodName,
							parameterTypes);

					if (implMethod.getDeclaringClass().equals(implPointer)) {
						return mProxy.invokeSuper(obj, args);
					} else if (fieldName != null) {
						Map<String, Field> cacheMap = getAutoSetCacheMap(implPointer);
						Field field = cacheMap.get(fieldName);
						if (field != null) {
							if (field.getType().isAssignableFrom(
									args[0].getClass())) {
								field.set(obj, args[0]);
								return null;
							} else {
								throw new RuntimeException("Feld " + field
										+ " nicht mit " + args[0].getClass()
										+ " von " + args[0] + " kompatibel");
							}
						}
					}
					implPointer = implPointer.getSuperclass();
				} while (!implPointer.equals(Object.class));

				throw new RuntimeException("Keine Implementierung von "
						+ method + " gefunden");
			}
		});
		return (T) enhancer.create();
	}

	protected static Map<String, Field> getAutoSetCacheMap(Class implPointer) {
		Map<String, Field> map = autoSetCacheMaps.get(implPointer);
		if (map == null) {
			map = new HashMap<String, Field>();
			for (Field f : implPointer.getDeclaredFields()) {
				AutoSet annotation = f.getAnnotation(AutoSet.class);
				if (annotation != null) {
					f.setAccessible(true);
					String name = annotation.value();
					if (!name.trim().isEmpty()) {
						map.put(name.toLowerCase(), f);
					} else {
						map.put(f.getName().toLowerCase(), f);
					}
				}
			}
			map = Collections.unmodifiableMap(map);
			autoSetCacheMaps.put(implPointer, map);
		}
		return map;
	}
	
}
