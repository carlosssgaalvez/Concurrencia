import java.util.concurrent.Semaphore;

public class Tren {
	private final int N = 5;

	private volatile int nPasajerosVagon1 = 0;
	private volatile int nPasajerosVagon2 = 0;

	private Semaphore puedoIniciarViaje = new Semaphore(0);
	private Semaphore puedoSubirTren = new Semaphore(1);
	private Semaphore puedoBajarVagon1 = new Semaphore(0);
	private Semaphore puedoBajarVagon2 = new Semaphore(0);

	private Semaphore mutex = new Semaphore(1);

	
	
	public void viaje(int id) throws InterruptedException {
		puedoSubirTren.acquire();
		mutex.acquire();
		
		if(nPasajerosVagon1 < N){ 	// Vagon 1
			nPasajerosVagon1++;
			System.out.println("Pasajero "+ id +" ha subido al vagon 1");
			mutex.release();
			puedoSubirTren.release();

			puedoBajarVagon1.acquire();
			mutex.acquire();
			nPasajerosVagon1--;
			if(nPasajerosVagon1 > 0){
				puedoBajarVagon1.release();
			} else {
				puedoBajarVagon2.release();
			}
			System.out.println("Pasajero "+ id +" ha bajado del vagon 1");
			mutex.release();

		} else {					// Vagon 2
			nPasajerosVagon2++;
			if(nPasajerosVagon2 < N){
				System.out.println("Pasajero "+ id +" ha subido al vagon 2");
				puedoSubirTren.release();
			} else {
				System.out.println("Pasajero "+ id +" ha subido al vagon 2");
				puedoIniciarViaje.release();
			}
			mutex.release();

			puedoBajarVagon2.acquire();
			mutex.acquire();
			nPasajerosVagon2--;
			if(nPasajerosVagon2 > 0){
				puedoBajarVagon2.release();
				System.out.println("Pasajero "+ id +" ha bajado del vagon 2");
			} else {
				puedoSubirTren.release();
				System.out.println("Pasajero "+ id +" ha bajado del vagon 2");
				System.out.println("******************************************");
			}
			
			mutex.release();
		}

	}

	public void empiezaViaje() throws InterruptedException {
		puedoIniciarViaje.acquire();
		System.out.println("        Maquinista:  empieza el viaje");

	}

	public void finViaje() throws InterruptedException  {
		
		System.out.println("        Maquinista:  fin del viaje");
		puedoBajarVagon1.release();
	}
}
