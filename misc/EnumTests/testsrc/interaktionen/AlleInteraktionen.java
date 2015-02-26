package interaktionen;

import java.util.LinkedList;
import java.util.List;

public class AlleInteraktionen {

	public interface Destroyable {

		void destroy();

	}

	private static ThreadLocal<List<Destroyable>> list = new ThreadLocal<List<Destroyable>>();

	public static void register(Destroyable destroyable) {
		get().add(destroyable);
	}

	private static List<Destroyable> get() {
		if (list.get() == null) {
			list.set(new LinkedList<Destroyable>());
		}
		return list.get();
	}
	
	public static void destroyAll() {
		for (Destroyable des : get()) {
			try {
				des.destroy();
			}catch (Throwable th) {
				th.printStackTrace();
			}
		}
		list.set(null);
	}

}
