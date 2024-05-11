package canibales;
import java.util.Random;

public class Canibal extends Thread {
	private Caldero cal;
	private int id;
	private Random r;
	
	public Canibal(int id, Caldero c) {
		this.id = id;
		cal =c;
		r = new Random();
	}
	public void run() {
		
		try {
			
			while(!this.isInterrupted()) {
				Thread.sleep(r.nextInt(50)); //baila
				cal.comer(id);
			}
		} catch (InterruptedException e) {
		}finally {
			System.out.println("Canibal "+id+" interrumpido");
		}
	}
}
