
public class Main {
	public static void main(String[] args) {
		System.out.println("<<<OLD>>>");
		TestOld.testIt();
		System.out.println("<<<NEW>>>");
		TestNew.testIt();
		System.out.println("<<<NEW2>>>");
		try {
		TestNew2.testIt();
		throw new RuntimeException("Exception expected");
		} catch (IllegalStateException e) {
			System.out.println("***"+e);
		}
	}
}
