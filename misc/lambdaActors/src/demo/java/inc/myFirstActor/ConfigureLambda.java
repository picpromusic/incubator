package inc.myFirstActor;

import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface ConfigureLambda {
	Future<Void> setImpl(Supplier<Stream<Integer>> generator);
}
