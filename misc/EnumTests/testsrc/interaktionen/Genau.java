package interaktionen;

public class Genau implements Bereich {
	
	private int nummer;

	public Genau(int i) {
		this.nummer = i;
	}

	@Override
	public int von() {
		return nummer;
	}

	@Override
	public int bis() {
		// TODO Auto-generated method stub
		return nummer;
	}

}
