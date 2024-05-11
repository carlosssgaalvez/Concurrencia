package sensores;


import java.util.Random;

public class Sensor extends Thread {
	private Sistema sis;
	private Random r;
	private int id;
	public Sensor(int id, Sistema s) {
		this.id = id;
		sis = s;
		r = new Random();
	}
	
	public void run() {
		try {
			while(!this.isInterrupted()) {
				int dato = r.nextInt(10);
				sis.ponerMedida(id, dato);
				Thread.sleep(r.nextInt(100));
			}
		} catch (InterruptedException e) {
			
		}finally {
			System.out.println("Sensor "+id+" interrumpido");
		}

	}
}
