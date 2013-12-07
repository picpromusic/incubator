package inc;

import java.util.concurrent.Future;
import java.util.function.BiFunction;

@FunctionalInterface
public interface ExternalLambda {
	public Future<String> calc(BiFunction<Integer, Integer, Integer> func);

}
