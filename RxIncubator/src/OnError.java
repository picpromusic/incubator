import rx.Observable;
import rx.Subscriber;

public class OnError {

	public void doIt() {
		Observable.OnSubscribe<String> subscribeFunction = (s) -> produceValuesAndAnError(s);

		Observable createdObservable = Observable.create(subscribeFunction);

		createdObservable.subscribe(
				(incomingValue) -> System.out.println("incoming "
						+ incomingValue),
				(error) -> System.out.println("Something went wrong "
						+ ((Throwable) error).getMessage()),
				() -> System.out.println("This observable is finished"));
	}

	private void produceValuesAndAnError(Subscriber s) {
		Subscriber subscriber = s;

		try {
			for (int ii = 0; ii < 50; ii++) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onNext("Pushed value " + ii);
				}

				if (ii == 5) {
					throw new Throwable("Something has gone wrong here");
				}
			}

			if (!subscriber.isUnsubscribed()) {
				subscriber.onCompleted();
			}
		} catch (Throwable throwable) {
			subscriber.onError(throwable);
		}
	}
	
	public static void main(String[] args) {
		new OnError().doIt();
	}
}
