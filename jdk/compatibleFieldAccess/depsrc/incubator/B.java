package incubator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class B extends StcA {
	
	@Override
	public String toString() {
		
		try {
			MethodHandle findSetter = MethodHandles.lookup().findSetter(B.class, "field", int.class);
			findSetter.invoke(this,42);
			MethodHandle findGetter = MethodHandles.lookup().findGetter(B.class, "field", int.class);
			Object o = findGetter.invoke(this);
			System.out.println(o);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int t = this.field;
		System.out.println("this:"+t);
		int s = super.field;
		System.out.println("super:"+s);
		return "B:" + t + " " + s;
	}
	
}
