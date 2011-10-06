package test;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;


public class M {
	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("START");
		G.call();
		System.out.println("END");
	}

	public static void cafeBabe() {
		System.out.println("DONE");
	}

	public static CallSite bootstrap(Lookup lookup, String name,
			MethodType type, int rightValue) throws NoSuchMethodException, IllegalAccessException {

		MethodHandle printArgs = lookup.findStatic(M.class, "cafeBabe",
				MethodType.methodType(void.class));
		System.out.println("Bootstrap");
		return new ConstantCallSite(printArgs.asType(type));
	}
}
