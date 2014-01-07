import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javalang.ref.Accessor;
import javalang.ref.AsymeticAcessorError;
import javalang.ref.UnambiguousFieldError;

public class Bootstrapper {

	private static boolean sol1;
	private static boolean sol2;
	private static boolean solBoot;

	static {
		String solution = System.getProperty("SolutionList");
		sol1 = solution != null ? solution.contains("1") : false; // Field
																	// resolution
																	// ambiguous
		sol2 = solution != null ? solution.contains("2") : false; // Field
																	// resolution
																	// unambiguous
		solBoot = solution != null ? solution.contains("B") : false; // Field
																		// resolution
																		// Bootstrap
		if (sol1 && sol2) {
			throw new IllegalStateException(
					"Solution 1 and 2 cannot be used together");
		}
		if ((sol1 || sol2) && solBoot) {
			throw new IllegalStateException(
					"Solution B cannot be used together with one of 1 or 2");
		}

		if (sol1) {
			throw new RuntimeException("SOL1 NYI");
		}
		if (solBoot) {
			throw new RuntimeException("SOL_BOOT NYI");
		}

	}

	public static CallSite getFunction(Lookup lookup, String name,
			MethodType type, String declaringClass, int mod)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		CallSite cs = innerGetFunction(false, lookup, name, type,
				declaringClass, mod);
		if (cs == null) {
			throw new VerifyError("No get accessor found for " + declaringClass
					+ "." + name);
		}
		return cs;
	}

	public static CallSite innerGetFunction(boolean symetryCheck,
			Lookup lookup, String name, MethodType type, String declaringClass,
			int mod) throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		if (sol2) {
			boolean staticProperty = Modifier.isStatic(mod);
			Class<?> clazz;
			if (!staticProperty) {
				clazz = type.parameterArray()[0];
			} else {
				clazz = Class.forName(declaringClass);
			}
			boolean potentialUnambiguousFieldDetected = false;
			while (!clazz.getCanonicalName().equals("java.lang.Object")) {
				Class<?> fieldType = type.returnType();
				try {
					if (!symetryCheck && potentialUnambiguousFieldDetected) {
						CallSite cs = findAccesorMethod(symetryCheck, true,
								lookup, name, type, mod, staticProperty, clazz,
								fieldType);
						if (cs != null) {
							throw new UnambiguousFieldError();
						}
					} else {
						if (!symetryCheck) {
							for (Field f : clazz.getDeclaredFields()) {
								potentialUnambiguousFieldDetected |= f
										.getName().equals(name);
							}
						}
						MethodHandle ret = staticProperty //
						? lookup.findStaticGetter(clazz, name, fieldType)
								: lookup.findGetter(clazz, name, fieldType);
						// Workaround till findStaticGetter/findGetter is fixed
						if (!clazz.getField(name).isAccessible()) {
							Field declaredField = clazz.getDeclaredField(name);
						}
						return new ConstantCallSite(ret);
					}
				} catch (Exception e) { // Should be
										// ReflectiveOperationException |
										// IllegalAccessException
					CallSite cs = findAccesorMethod(symetryCheck, true, lookup,
							name, type, mod, staticProperty, clazz, fieldType);
					if (cs != null) {
						if (!symetryCheck && potentialUnambiguousFieldDetected) {
							throw new UnambiguousFieldError();
						}
						return cs;
					}
				}
				clazz = clazz.getSuperclass();
			}

			throw new VerifyError("No get accessor found for "
					+ clazz.getCanonicalName() + "." + name);
		}
		throw new RuntimeException("Forgot to  choose an implemented algorith");
	}

	private static CallSite findAccesorMethod(boolean symetryCheck,
			boolean get, Lookup lookup, String name, MethodType type, int mod,
			boolean staticProperty, Class<?> clazz, Class<?> fieldType)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException, AsymeticAcessorError {
		Method[] methods = clazz.getDeclaredMethods();
		Class<?> expectedRetType = get ? type.returnType() : void.class;
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers()) == staticProperty
					&& method.getReturnType().equals(expectedRetType)) {
				Accessor annotation = method.getAnnotation(Accessor.class);
				if (annotation != null && annotation.value().equals(name)) {
					MethodType mt = get ? MethodType.methodType(fieldType)
							: MethodType.methodType(void.class,
									method.getParameterTypes());
					MethodHandle ret = staticProperty //
					? lookup.findStatic(clazz, method.getName(), mt)
							: lookup.findVirtual(clazz, method.getName(), mt);
					if (!symetryCheck) {
						symetryCheck(false, lookup, name, mod, staticProperty,
								clazz, fieldType);
					}
					return new ConstantCallSite(ret.asType(type));
				}
			}
		}
		return null;
	}

	private static CallSite findAccesorSetMethod(boolean symetryCheck,
			Lookup lookup, String name, MethodType type, int mod,
			boolean staticProperty, Class<?> clazz, Class<?> fieldType)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException, AsymeticAcessorError {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers()) == staticProperty
					&& method.getReturnType().equals(void.class)) {
				Accessor annotation = method.getAnnotation(Accessor.class);
				if (annotation != null && annotation.value().equals(name)) {
					MethodType mt = MethodType.methodType(void.class,
							method.getParameterTypes());
					MethodHandle ret = staticProperty ? lookup.findStatic(
							clazz, method.getName(), mt) : lookup.findVirtual(
							clazz, method.getName(), mt);
					if (!symetryCheck) {
						symetryCheck(true, lookup, name, mod, staticProperty,
								clazz, fieldType);
					}
					return new ConstantCallSite(ret.asType(type));
				}
			}
		}
		return null;
	}

	private static void symetryCheck(boolean checkGet, Lookup lookup,
			String name, int mod, boolean staticProperty, Class<?> clazz,
			Class<?> fieldType) throws NoSuchMethodException,
			IllegalAccessException, ClassNotFoundException,
			AsymeticAcessorError {
		ArrayList<Class<?>> ptypes = new ArrayList<>(2);
		Class<?> rType = Void.class;
		if (!staticProperty) {
			ptypes.add(clazz);
		}
		CallSite callSite = null;
		if (checkGet) {
			rType = fieldType;
			callSite = innerGetFunction(true, lookup,//
					name,//
					MethodType.methodType(rType, ptypes),//
					clazz.getCanonicalName().replace('.', '/'),//
					mod);
		} else {
			ptypes.add(fieldType);
			callSite = innerSetFunction(true, lookup,//
					name,//
					MethodType.methodType(rType, ptypes),//
					clazz.getCanonicalName().replace('.', '/'),//
					mod);
		}
		if (callSite == null) {
			throw new AsymeticAcessorError(clazz, name);
		}
	}

	public static CallSite setFunction(Lookup lookup, String name,
			MethodType type, String declaringClass, int mod)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		CallSite cs = innerSetFunction(false, lookup, name, type,
				declaringClass, mod);
		if (cs == null) {
			throw new VerifyError("No set accessor found for " + declaringClass
					+ "." + name);
		}
		return cs;
	}

	public static CallSite innerSetFunction(boolean symetryCheck,
			Lookup lookup, String name, MethodType type, String declaringClass,
			int mod) throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		boolean staticProperty = Modifier.isStatic(mod);
		Class<?> clazz;
		if (!staticProperty) {
			clazz = type.parameterArray()[0];
		} else {
			clazz = Class.forName(declaringClass);
		}
		if (sol2) {
			boolean potentialUnambiguousFieldDetected = false;
			while (!clazz.getCanonicalName().equals("java.lang.Object")) {
				Class<?> fieldType = type.parameterType(staticProperty ? 0 : 1);
				try {
					if (!symetryCheck && potentialUnambiguousFieldDetected) {
						CallSite cs = findAccesorMethod(symetryCheck, false,
								lookup, name, type, mod, staticProperty, clazz,
								fieldType);
						if (cs != null) {
							throw new UnambiguousFieldError();
						}
					} else {
						if (!symetryCheck) {
							for (Field f : clazz.getDeclaredFields()) {
								potentialUnambiguousFieldDetected |= f
										.getName().equals(name);
							}
						}
						MethodHandle ret = staticProperty //
						? lookup.findStaticSetter(clazz, name, fieldType)//
								: lookup.findSetter(clazz, name, fieldType);
						// Workaround till findStaticSetter/findSetter is fixed
						if (!clazz.getField(name).isAccessible()) {
							Field declaredField = clazz.getDeclaredField(name);
						}
						return new ConstantCallSite(ret);
					}
				} catch (Exception e) { // Should be
										// ReflectiveOperationException |
										// IllegalAccessException
					CallSite cs = findAccesorMethod(symetryCheck, false, lookup,
							name, type, mod, staticProperty, clazz, fieldType);
					if (cs != null) {
						if (!symetryCheck && potentialUnambiguousFieldDetected) {
							throw new UnambiguousFieldError();
						}
						return cs;
					}
				}
				clazz = clazz.getSuperclass();
			}
			return null;
		}
		throw new RuntimeException("Forgot to  choose an implemented algorith");

	}

}
