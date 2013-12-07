package inc;

import static lambdaActors.ActorSystem.nothing;
import static lambdaActors.ActorSystem.thrownException;
import static lambdaActors.ActorSystem.value;
import inc.myFirstActor.ConfigureLambda;
import inc.myFirstActor.ExternalLambda;
import inc.myFirstActor.GetSomeNumber;
import inc.myFirstActor.MyFirstActorInterface;
import inc.myFirstActor.PrintMe;
import inc.myFirstActor.ThrowsException;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lambdaActors.ActorImpl;
import lambdaActors.ActorMessage;

public class MyFirstActorImpl extends
		ActorImpl<MyFirstActorInterface, MyFirstActorImpl.Data> {

	public static class Data {
		public Supplier<Stream<Integer>> generator;
	}

	public MyFirstActorImpl() {
		super(MyFirstActorInterface.class);
		bake();
	}

	@ActorMessage(ConfigureLambda.class)
	public static Future<Void> generator(Supplier<Stream<Integer>> generator) {
		getData().generator = generator;
		return nothing();
	}
	
	protected static Data getData() {
		return (Data) ActorImpl.getData();
	}

	@ActorMessage(PrintMe.class)
	public static void printMe() {
		System.out.println("MyFirstActorImpl.printMe()");
		System.out.println("Hello from printMe");
	}

	@ActorMessage(ExternalLambda.class)
	public static String executeComplexLambdaScenario(
			BiFunction<Integer, Integer, Integer> f) {
		Supplier<Stream<Integer>> generator = getData().generator;
		if (generator != null) {
			Iterator<Integer> stream = generator.get().iterator();
			int result = stream.hasNext() ? stream.next() : 0;
			while (stream.hasNext()) {
				result = f.apply(result, stream.next());
			}
			return "Result: " + result;
		}else {
			return "Result:" + f.apply(5, 7);
		}
	}

	@ActorMessage()
	public static GetSomeNumber<Integer> getNumber() {
		return (boolean suppress) -> {
			System.out.println("MyFirstActorImpl.getNumber()");
			Future<Integer> ret = value(42);
			if (suppress) {
				throw new RuntimeException("Suppressing the value. Harharhar");
			}
			return ret;
		};
	}

	@ActorMessage()
	public static ThrowsException throwTest() {
		return (boolean twice) -> {
			if (twice) {
				thrownException(new IOException("suppressed"));
			}
			throw new IOException("TestException");
		};
	}

	@Override
	protected Data createNewData() {
		return new Data();
	}

}
