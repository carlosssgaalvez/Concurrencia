package prod_cons;

public class Consumidor extends Thread {
	private Buffer b;
	private int it;
	
	public Consumidor(Buffer b, int it) {
		this.b = b;
		this.it = it;
	}
	public void run() {
		for(int i =0; i<it; i++) {
			b.consumir();			
		}
	}
}
