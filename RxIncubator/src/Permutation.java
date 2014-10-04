import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;

public class Permutation {

	public static Observable<List<Integer>> createPermatationObserver(int... o) {
		return PermutationWithStartVector.createPermutationObserver(o,
				Observable.just(0).repeat());
	}

}
