import rx.Observable;

public class Hello {
	public static void hello(String... names) {
		Observable.from(names).subscribe(s -> {
			System.out.println("Hello " + s + "!");
		});
	}

	public static void main(String[] args) {
		hello("1", "Welt", "Sebastian");
	}
}
