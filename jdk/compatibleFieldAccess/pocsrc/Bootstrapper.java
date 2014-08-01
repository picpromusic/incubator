import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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
		if (sol2 | sol1) {
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
						if (!symetryCheck && sol2) {
							for (Field f : clazz.getDeclaredFields()) {
								potentialUnambiguousFieldDetected |= f
										.getName().equals(name);
							}
						}
						MethodHandle ret = staticProperty //
						? lookup.findStaticGetter(clazz, name, fieldType)
								: lookup.findGetter(clazz, name, fieldType);
						// Workaround till findStaticGetter/findGetter is fixed
						if (!isFieldAccessable(lookup, clazz, name)) {
							throw new IllegalAccessException(clazz + "." + name
									+ " is not accessable from "
									+ lookup.lookupClass());
						}
						if (!clazz.getField(name).isAccessible()) {
							clazz.getDeclaredField(name);
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
		Method resolvedMethod = null;
		MethodHandle resolvedHandle = null;
		List<IllegalAccessException> suppressed = new ArrayList<>();
		for (Method method : methods) {
			String methodName = method.getName();
			if (method.getReturnType().equals(expectedRetType)) {
				Accessor annotation = method.getAnnotation(Accessor.class);
				if (annotation != null) {
					String fieldName = AccessorMethodUtil.determineAccessedFieldname(get, methodName, annotation.value());

					if (fieldName.equals(name)) {
						if (Modifier.isStatic(method.getModifiers()) != staticProperty) {
							String message = MessageFormat
									.format("{0}-Field {1} is not accessable in a {2} way",
											(staticProperty ? "Instance"
													: "Static"), name,
											(staticProperty ? "static"
													: "non-static"));
							if (sol2) {
								throw new IncompatibleClassChangeError(message);
							}
						}

						MethodType mt = get ? MethodType.methodType(fieldType)
								: MethodType.methodType(void.class,
										method.getParameterTypes());
						try {
							MethodHandle tempHandle = staticProperty //
							? lookup.findStatic(clazz, method.getName(), mt)
									: lookup.findVirtual(clazz,
											method.getName(), mt);
							if (isMethodAccessable(lookup, method)) {
								if (resolvedMethod == null
										|| isMoreSpecific(resolvedMethod,
												method, name)) {
									resolvedMethod = method;
									resolvedHandle = tempHandle;
								}
							} else {
								throw new IllegalAccessException(method
										+ " is not accessable from "
										+ lookup.lookupClass());
							}
						} catch (IllegalAccessException e) {
							if (sol2) {
								throw e;
							} else {
								suppressed.add(e);
							}
						}
					}
				}
			}
		} // end for
		if (resolvedHandle != null) {
			if (!symetryCheck) {
				symetryCheck(false, lookup, name, mod, staticProperty, clazz,
						fieldType);
			}
			return new ConstantCallSite(resolvedHandle.asType(type));
		} else {
			if (!suppressed.isEmpty()) {
				IllegalAccessException e = new IllegalAccessException(
						"One or more IllegalAccessException thrown");
				for (IllegalAccessException illegalAccessException : suppressed) {
					e.addSuppressed(illegalAccessException);
				}
				throw e;
			}
			return null;
		}
	}

	private static boolean isFieldAccessable(Lookup lookup, Class<?> clazz,
			String name) {
		try {
			return isAccessable(lookup.lookupClass(), clazz,
					clazz.getField(name).getModifiers());

		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		}
	}

	private static boolean isAccessable(Class<?> lookupClass, Class<?> clazz,
			int mod) {
		if (Modifier.isPublic(mod) || lookupClass.equals(clazz)) {
			return true;
		} else if (Modifier.isPrivate(mod)) {
			return false;
		} else if (Modifier.isProtected(mod)) {
			if (clazz.isAssignableFrom(lookupClass)) {
				return true;
			} else {
				return false;
			}
		} else /* package */{
			return lookupClass.getPackage().equals(clazz.getPackage());
		}

	}

	private static boolean isMethodAccessable(Lookup lookup, Method method) {
		return isAccessable(lookup.lookupClass(), method.getDeclaringClass(),
				method.getModifiers());
	}

	/**
	 * <pre>
	 * |pre  new->|public               |package              |protected            |private              |
	 * ||         |                     |                     |                     |                     |
	 * |v         |                     |                     |                     |                     |
	 * ----------------------------------------------------------------------------------------------------
	 * |public    |UnambiguousFieldError|true                 |true                 |true                 |
	 * |package   |false                |UnambiguousFieldError|UnambiguousFieldError|true                 |
	 * |protected |false                |UnambiguousFieldError|UnambiguousFieldError|true                 |
	 * |private   |false                |false                |false                |UnambiguousFieldError|
	 * </pre>
	 * 
	 * @param resolvedMethod
	 * @param method
	 * @param fieldName
	 * @return
	 */
	private static boolean isMoreSpecific(Method resolvedMethod, Method method,
			String fieldName) {
		int visibilityMask = Modifier.PUBLIC | Modifier.PROTECTED
				| Modifier.PRIVATE;
		int actMod = resolvedMethod.getModifiers() & visibilityMask;
		int newMod = method.getModifiers() & visibilityMask;
		if ((actMod & newMod & Modifier.PUBLIC) != 0) {
			throw new UnambiguousFieldError(sameVibilityMessage(fieldName,
					Modifier.PUBLIC));
		} else if ((actMod & newMod & Modifier.PROTECTED) != 0) {
			throw new UnambiguousFieldError(sameVibilityMessage(fieldName,
					Modifier.PROTECTED));
		} else if ((actMod & newMod & Modifier.PRIVATE) != 0) {
			throw new UnambiguousFieldError(sameVibilityMessage(fieldName,
					Modifier.PRIVATE));
		} else if (isPackage(actMod) && isPackage(newMod)) {
			throw new UnambiguousFieldError(sameVibilityMessage(fieldName,
					"<<Package-Visible>>"));
		} else if ((Modifier.isProtected(actMod) && isPackage(newMod))
				|| (Modifier.isProtected(newMod) && isPackage(actMod))) {
			throw new UnambiguousFieldError(incompatibileVibilityMessage(
					fieldName, Modifier.PROTECTED, "<<Package-Visible>>"));
		} else if (Modifier.isPublic(actMod)) {
			return true;
		} else if (Modifier.isPrivate(actMod)) {
			return false;
		} else /* actMod == PACKAGE */{
			return Modifier.isPrivate(newMod); // Public will be false
		}
	}

	private static String sameVibilityMessage(String fieldName, int mod) {
		return sameVibilityMessage(fieldName, Modifier.toString(mod));
	}

	private static String sameVibilityMessage(String fieldName,
			String modAsstring) {
		return "Two Access-Methods with same visibility(" + modAsstring
				+ ") for field " + fieldName;
	}

	private static String incompatibileVibilityMessage(String fieldName,
			int mod1, String mod2AsString) {
		return "Two Access-Methods with incompatible visibility("
				+ Modifier.toString(mod1) + " & " + mod2AsString
				+ " ) for field " + fieldName;
	}

	private static boolean isPackage(int mod) {
		return !Modifier.isPublic(mod) && !Modifier.isPrivate(mod)
				&& !Modifier.isProtected(mod);
	}

	private static void throwSameVisibilityException(String fieldName,
			int actMod) throws UnambiguousFieldError {
		throw new UnambiguousFieldError(
				"Two Access-Methods with same visibility("
						+ Modifier.toString(actMod) + ") for field "
						+ fieldName);
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
					clazz.getCanonicalName(),//
					mod);
		} else {
			ptypes.add(fieldType);
			callSite = innerSetFunction(true, lookup,//
					name,//
					MethodType.methodType(rType, ptypes),//
					clazz.getCanonicalName(),//
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
		if (sol2 | sol1) {
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
						if (!symetryCheck && sol2) {
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
							clazz.getDeclaredField(name);
						}
						return new ConstantCallSite(ret);
					}
				} catch (Exception e) { // Should be
										// ReflectiveOperationException |
										// IllegalAccessException
					CallSite cs = findAccesorMethod(symetryCheck, false,
							lookup, name, type, mod, staticProperty, clazz,
							fieldType);
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
