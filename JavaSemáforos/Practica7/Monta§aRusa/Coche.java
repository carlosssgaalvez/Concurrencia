
import java.util.concurrent.Semaphore;

public class Coche extends Thread {
	private int capacidad;
	private volatile int nPasajeros;
	private Semaphore puedeViajar, puedeSubirsePasajero, puedeBajarsePasajero;
	
	public Coche(int capacidad) {
		this.capacidad = capacidad;
		nPasajeros = 0;
		puedeSubirsePasajero = new Semaphore(1);
		puedeBajarsePasajero = new Semaphore(0);
		puedeViajar = new Semaphore(0);
		
	}
	public void quieroSubir(int id) throws InterruptedException{
		puedeSubirsePasajero.acquire();

		nPasajeros++;
		System.out.println("Pasajero: " + id + " se ha subido");
		if(nPasajeros < capacidad){
			puedeSubirsePasajero.release();
		} else if( nPasajeros == capacidad){
			puedeViajar.release();
		}
		

	}

	public void quieroBajar(int id)  throws InterruptedException{
		puedeBajarsePasajero.acquire();

		nPasajeros--;
		System.out.println("Pasajero: " + id + " se ha bajado");
		if(nPasajeros > 0){
			puedeBajarsePasajero.release();
		} else if(nPasajeros == 0){
			System.out.println("SIGUIENTE VIAJE DISPONIBLE");
			puedeSubirsePasajero.release();
		}

	}
	
	public void inicioViaje() throws InterruptedException {		
		puedeViajar.acquire();
		System.out.println("Viaje iniciado");
	}
	
	public void finViaje() throws InterruptedException {							
		System.out.println("Viaje finalizado");
		puedeBajarsePasajero.release();
	}
	
	
	public void run() {
		try {
			while(!isInterrupted()) {
				inicioViaje();
				Thread.sleep(100);
				finViaje();
			}
		}catch(InterruptedException e) {
			System.out.println("Coche interrumpido");
		}
	}
	
	public static void main(String[] args) {
		Coche c = new Coche(4);
		Pasajero pasajeros[] = new Pasajero[10];
		for(int i = 0; i <pasajeros.length ; i++)
			pasajeros[i] = new Pasajero(i, c);
		c.start();
		for(int i = 0; i <pasajeros.length ; i++)
			pasajeros[i].start();

	}

}
