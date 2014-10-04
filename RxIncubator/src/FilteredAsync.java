import rx.Observable;
import rx.Subscriber;

public class FilteredAsync {

	public void doIt() {
		Observable.OnSubscribe<String> subscribeFunction = (s) -> asyncProcessingOnSubscribe(s);

		Observable asyncObservable = Observable.create(subscribeFunction);

		asyncObservable.skip(5).subscribe(
				(incomingValue) -> System.out.println(incomingValue));

	}

	private void asyncProcessingOnSubscribe(Subscriber s) {
		final Subscriber<String> subscriber = s;
		Thread thread = new Thread(() -> produceSomeValues(subscriber));
		thread.start();
	}

	private void produceSomeValues(Subscriber<String> subscriber) {
		for (int ii = 0; ii < 10; ii++) {
			if (!subscriber.isUnsubscribed()) {
				subscriber.onNext("Pushing value from async thread " + ii);
			}
		}
	}

	public static void main(String[] args) {
		new FilteredAsync().doIt();
	}

}
