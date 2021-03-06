package incubator.cfa;

import incubator.cfa.jdk.Accessor;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Bootstrapper {
	public static CallSite getFunction(Lookup lookup, String name,
			MethodType type, String declaringClass, int mod)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		boolean staticProperty = Modifier.isStatic(mod);
		Class<?> clazz;
		if (!staticProperty) {
			clazz = type.parameterArray()[0];
		} else {
			clazz = Class.forName(declaringClass);
		}
		try {
			MethodHandle ret = staticProperty //
			? lookup.findStaticGetter(clazz, name, type.returnType())
					: lookup.findGetter(clazz, name, type.returnType());
			return new ConstantCallSite(ret);
		} catch (Exception e) { // Should be ReflectiveOperationException |
								// IllegalAccessException
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (Modifier.isStatic(method.getModifiers()) == staticProperty
						&& method.getReturnType().equals(type.returnType())) {
					Accessor annotation = method.getAnnotation(Accessor.class);
					if (annotation != null && annotation.value().equals(name)) {
						MethodType mt = MethodType
								.methodType(type.returnType());
						MethodHandle ret = staticProperty //
						? lookup.findStatic(clazz, method.getName(), mt)
								: lookup.findVirtual(clazz, method.getName(),
										mt);
						return new ConstantCallSite(ret.asType(type));
					}
				}
			}
		}

		throw new VerifyError("No get accessor found for "
				+ clazz.getCanonicalName() + "." + name);
	}

	public static CallSite setFunction(Lookup lookup, String name,
			MethodType type, String declaringClass, int mod)
			throws NoSuchMethodException, IllegalAccessException,
			ClassNotFoundException {
		boolean staticProperty = Modifier.isStatic(mod);
		Class<?> clazz;
		if (!staticProperty) {
			clazz = type.parameterArray()[0];
		} else {
			clazz = Class.forName(declaringClass);
		}
		try {
			MethodHandle ret = staticProperty //
			? lookup.findStaticSetter(clazz, name, type.parameterType(0))
					: lookup.findSetter(clazz, name, type.parameterType(1));

			return new ConstantCallSite(ret);
		} catch (Exception e) { // Should be ReflectiveOperationException |
								// IllegalAccessException
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (Modifier.isStatic(method.getModifiers()) == staticProperty
						&& method.getReturnType().equals(void.class)) {
					Accessor annotation = method.getAnnotation(Accessor.class);
					if (annotation != null && annotation.value().equals(name)) {
						MethodType mt = MethodType.methodType(void.class,
								method.getParameterTypes());
						MethodHandle ret = staticProperty ? lookup.findStatic(
								clazz, method.getName(), mt) : lookup.findVirtual(clazz,
								method.getName(), mt);
						return new ConstantCallSite(ret.asType(type));
					}
				}
			}
		}

		throw new VerifyError("No set accessor found for "
				+ clazz.getCanonicalName() + "." + name);
	}

}
