import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;

public class PermutationTest {
	
	private static void printAll(Observable<List<Integer>> create) {
		create.forEach(
				//
				(i) -> {
					System.out.println(i);
				}, (Throwable th) -> System.out.println("Fehler " + th),
				() -> System.out.println("Ready"));
	}

	public static void main(String[] args) {
		Observable<List<Integer>> perms = Permutation
				.createPermatationObserver(1, 2, 3);
		printAll(perms);

		Observable<List<Integer>> createPermutationObserver = PermutationWithStartVector
				.createPermutationObserver(new int[] { 1, 2, 3 },
						new int[] { 2 });
		printAll(createPermutationObserver);

		createPermutationObserver = PermutationWithStartVector
				.createPermutationObserver(new int[] { 1, 2, 3, 4, 5 },
						new int[] { 3, 3, 2, 1, 0 });
		printAll(createPermutationObserver);

	}

}
