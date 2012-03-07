import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Bootstrapper {
	public static CallSite getFunction(Lookup lookup, String name,
			MethodType type) throws NoSuchMethodException,
			IllegalAccessException, ClassNotFoundException {
		Class<?> clazz = type.parameterArray()[0];
		try {
			MethodHandle ret = lookup
					.findGetter(clazz, name, type.returnType());
			return new ConstantCallSite(ret);
		} catch (Exception e) { // Should be ReflectiveOperationException | IllegalAccessException
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getReturnType().equals(type.returnType())) {
					AccessorMethod annotation = method.getAnnotation(AccessorMethod.class);
					if (annotation != null && annotation.value().equals(name)) {
						// System.out.println(method + " "
						// + Arrays.toString(method.getAnnotations()));
						MethodType mt = MethodType
								.methodType(type.returnType());
						MethodHandle ret = lookup.findVirtual(clazz,
								method.getName(), mt);
						return new ConstantCallSite(ret.asType(type));
					}
				}
			}
		}

		throw new VerifyError("No get accessor found for "
				+ clazz.getCanonicalName() + "." + name);
	}

	public static CallSite setFunction(Lookup lookup, String name,
			MethodType type) throws NoSuchMethodException,
			IllegalAccessException, ClassNotFoundException {
		Class<?> clazz = type.parameterArray()[0];
		try {
			MethodHandle ret = lookup
					.findSetter(clazz, name, type.parameterType(1));
			return new ConstantCallSite(ret);
		} catch (Exception e) { // Should be ReflectiveOperationException | IllegalAccessException
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getReturnType().equals(void.class)) {
					AccessorMethod annotation = method.getAnnotation(AccessorMethod.class);
					if (annotation != null && annotation.value().equals(name)) {
						// System.out.println(method + " "
						// + Arrays.toString(method.getAnnotations()));
						MethodType mt = MethodType.methodType(void.class,
								method.getParameterTypes());
						MethodHandle ret = lookup.findVirtual(clazz,
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