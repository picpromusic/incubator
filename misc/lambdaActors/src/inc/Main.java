package inc;

import inc.MyFirstActorImpl.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import lambdaActors.ActorImpl;

public class Main {

	public static void main(String[] args) {
		try (ActorImpl<MyFirstActorInterface, Data> act = new MyFirstActorImpl()) {

			MyFirstActorInterface myActor = act.createNew();
			myActor.printMe();
			Future<Integer> someNumber = myActor.getSomeNumber(false);
			System.out.println(someNumber.get());

			testException(myActor, false);
			testException(myActor, true);

			someNumber = myActor.getSomeNumber(true);
			try {
				someNumber.get();
			} catch (ExecutionException e) {
				System.out.println(e.getCause() + " "
						+ Arrays.toString(e.getCause().getSuppressed()));
			}
			Future<String> result = myActor.calc((t, u) -> {
				return t + u;
			});
			myActor.setImpl(() -> {
				ArrayList<Integer> values = new ArrayList<>();
				values.add(5);
				values.add(7);
				values.add(9);
				values.add(23);
				return values.parallelStream();
			});
			Future<String> result2 = myActor.calc((t, u) -> {
				return t + u;
			});
			Future<String> result3 = myActor.calc((t, u) -> {
				return t * u;
			});
			myActor.setImpl(() -> {
				ArrayList<Integer> values = new ArrayList<>();
				values.add(2);
				values.add(2);
				values.add(3);
				values.add(3);
				return values.parallelStream();
			});
			Future<String> result4 = myActor.calc((t, u) -> {
				int temp = t+u;
				System.out.println(t + " " + u + " -> " + temp);
				return temp;
			});
			System.out.println(result.get());
			System.out.println(result2.get());
			System.out.println(result3.get());
			System.out.println(result4.get());

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	private static void testException(MyFirstActorInterface myActor,
			boolean twice) throws InterruptedException {
		try {
			Future<Void> throwsException = myActor.throwsException(twice);
			throwsException.get();
		} catch (ExecutionException e) {
			System.out.println(e.getCause() + " "
					+ Arrays.toString(e.getCause().getSuppressed()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
