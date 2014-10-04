import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class PermutationWithStartVector {

	public static Observable<List<Integer>> createPermutationObserver(int[] o,
			Observable<Integer> sVec) {

		if (o == null || o.length == 0) {
			return Observable.create((aSubscriber) -> {
				if (aSubscriber.isUnsubscribed()) {
					return;
				}
				aSubscriber.onCompleted();
			});
		} else if (o.length == 1) {
			return Observable.create((aSubscriber) -> {
				if (aSubscriber.isUnsubscribed()) {
					return;
				}
				Integer first = sVec.first().toBlocking().first();
				if (first != 0) {
					aSubscriber.onError(new RuntimeException(
							"Wrong SVector-Index for last element"));
				}
				ArrayList<Integer> arrayList = new ArrayList<Integer>(1);
				arrayList.add(o[0]);
				aSubscriber.onNext(arrayList);
				aSubscriber.onCompleted();
			});
		} else {
			return Observable.concat(createSubPermutationObservers(o, sVec));
		}
	}

	private static Observable createSubPermutationObservers(int[] o,
			Observable<Integer> sVec) {
		return Observable.create((aSubscriber) -> {
			if (aSubscriber.isUnsubscribed()) {
				return;
			}
			int start = sVec.first().toBlocking().single();
			if (start >= o.length) {
				aSubscriber.onError(new RuntimeException("Wrong SVector-Index("
						+ start + ") for Element"));
			}
			for (int i = start; i < o.length; i++) {
				// System.out.println(Arrays.toString(o));
				int j = o[i];
				int subO[] = new int[o.length - 1];
				System.arraycopy(o, 0, subO, 0, i);
				System.arraycopy(o, i + 1, subO, i, o.length - (i + 1));
				Observable<Integer> subSVec = i == start ? sVec.skip(1)
						: Observable.just(0).repeat();
				aSubscriber.onNext(prepend(j,
						createPermutationObserver(subO, subSVec)));
			}
			aSubscriber.onCompleted();
		});
	}

	private static Observable<List<Integer>> prepend(int j,
			Observable<List<Integer>> premObserver) {
		return premObserver.map((list) -> {
			list.add(0, j);
			return list;
		});
	}

	public static Observable<List<Integer>> createPermutationObserver(int[] o,
			int[] sVec) {
		Observable<Integer> sVecOb = toObserver(sVec);
		return createPermutationObserver(o,
				Observable.concat(sVecOb, Observable.just(0).repeat()));
	}

	private static Observable<Integer> toObserver(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>(array.length);
		for (Integer i : array) {
			list.add(i);
		}
		return Observable.from(list);
	}

}
