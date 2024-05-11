package prod_cons;


public class Productor extends Thread {
	private Buffer b;
	private int it;
	
	public Productor(Buffer b, int it) {
		this.b = b;
		this.it = it;
	}
	
	public void run() {
		for(int i=0;i < it; i++) {
			b.producir(i);
		}
	}
	
}
