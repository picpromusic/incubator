package inc.myFirstActor;

import java.util.concurrent.Future;

@FunctionalInterface
public interface GetSomeNumber<T extends Number> {
	public Future<T> getSomeNumber(boolean suppressWithException);
}
