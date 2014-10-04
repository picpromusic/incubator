import java.util.Arrays;

public class PermutationRec {

	private void doIt() {
		int[] perm = new int[] { 1 };
		System.out.println(Arrays.toString(perm));
		while (hasNext(perm)) {
			perm = nextPerm(perm);
		}

	}

	private int[] nextPerm(int[] perm) {
		return null;
	}

	private boolean hasNext(int[] perm) {
		if (perm.length == 1) {
			return false;
		} else {
			int[] rest = rest(perm);
			if (perm[0] < biggest(perm[0],rest)) {
				return true;
			}
			return hasNext(rest);
		}
	}

	private int[] rest(int[] array) {
		int[] rest = new int[array.length-1];
		System.arraycopy(array, 1, rest, 0, rest.length);
		return rest;
	}

	private int biggest(int biggest, int[] rest) {
		if (rest.length == 0) {
			return biggest;
		}else {
			biggest = biggest > rest[0] ? biggest : rest[0];
			return biggest(biggest,rest);
		}
	}

	public static void main(String[] args) {
		new PermutationRec().doIt();
	}
}
