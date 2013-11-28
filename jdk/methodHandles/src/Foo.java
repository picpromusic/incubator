import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;

public class Foo extends Bar {

	public Foo() {
		// All lookups will be done with the Lookup for the class Foo.
		// So
		Lookup fooLookup = MethodHandles.lookup();
		checkAccessToBarsPrivateFieldIsNotPermitted(fooLookup);

		// OK. We checked that we cannot access the private field.
		// See also The Java™ Virtual Machine Specification (page:356)
		// cite: (In general, resolving a method handle can be done in exactly
		// the same circumstances that the Java virtual machine would
		// successfully resolve the symbolic references in the bytecode
		// behavior. In particular, method handles to private and protected
		// members can be created in exactly those classes for which
		// the corresponding normal accesses are legal.)

		try {
			// Lets try with another Class (ok, it's me. So i should have access
			// to all MY fields.
			MethodHandle setter = fooLookup.findSetter(Foo.class,
					"myPrivateField", int.class);
			MethodHandle getter = fooLookup.findGetter(Foo.class,
					"myPrivateField", int.class);
			try {
				setter.invokeExact(this, 100);
				System.out.println("Initialized Bars privateField with "
						+ getter.invoke(this));
			} catch (Throwable e) {
				e.printStackTrace();
			}
			// Nice. But wait. myPrivateField is not my field. In fact it is
			// the field of BarBase.
			// Commented out in order to show the result of toString()
			// throw new AssertionError(
			// "Should not access a private field in super class");
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * We should not be able to access the privateField of Bar. So we check if
	 * this works;
	 * 
	 * @param fooLookup
	 * 
	 * @throws AssertionError
	 */
	private void checkAccessToBarsPrivateFieldIsNotPermitted(Lookup fooLookup)
			throws AssertionError {
		checkAccessIsDeniedForSearchRoot(fooLookup, BarBase.class);
		checkAccessIsDeniedForSearchRoot(fooLookup, Bar.class);
	}

	private void checkAccessIsDeniedForSearchRoot(Lookup fooLookup,
			Class<?> searchRoot) throws AssertionError {
		try {
			fooLookup.findGetter(searchRoot, "myPrivateField", int.class);
			throw new AssertionError("");
		} catch (final NoSuchFieldException e) {
			throw new AssertionError("");
		} catch (final IllegalAccessException e) {
			// Everything is fine
		}
		try {
			fooLookup.findSetter(searchRoot, "myPrivateField", int.class);
			throw new AssertionError("");
		} catch (final NoSuchFieldException e) {
			throw new AssertionError("");
		} catch (final IllegalAccessException e) {
			// Everything is fine
		}
	}

	private void bamm() {
		try {
			MethodHandle registerNatives = MethodHandles.lookup().findStatic(
					Foo.class,
					"registerNatives",
					MethodType.fromMethodDescriptorString("()V",
							Foo.class.getClassLoader()));
			registerNatives.invoke();
		} catch (NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | TypeNotPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Foo () extends Bar(" + super.toString() + ")";
	}

	public static void main(String[] args) {
		Foo foo = new Foo();
		System.out.println(foo);
		foo.bamm();
	}
}
