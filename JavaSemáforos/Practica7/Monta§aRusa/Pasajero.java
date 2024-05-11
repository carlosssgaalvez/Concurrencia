

public class Pasajero extends Thread {
	private int id;
	private Coche coche;
	
	public Pasajero(int id, Coche c) {
		coche = c;
		this.id = id;
	}
	
	public void run() {
		try {
		while(!isInterrupted()) {
			Thread.sleep(100);
			coche.quieroSubir(id);
			coche.quieroBajar(id);
		}
		}catch(InterruptedException e) {
			System.out.println("Pasajero"+id+" interrumpido");
		}
		
	}
}
