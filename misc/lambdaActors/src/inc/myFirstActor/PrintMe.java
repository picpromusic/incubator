package inc.myFirstActor;

import java.util.concurrent.Future;

@FunctionalInterface
public interface PrintMe {
	Future<Void> printMe();
}
