import java.util.concurrent.Semaphore;

public class Barca {
	private volatile int nPasajeros;
	private volatile int orilla = 1;

	//norte 1 sur 0
	
	private Semaphore puedoSubirBarcaSur = new Semaphore(0);
	private Semaphore puedoSubirBarcaNorte = new Semaphore(1);
	private Semaphore puedoBajarBarca = new Semaphore(0);
	private Semaphore puedoIniciarViaje = new Semaphore(0);
	private Semaphore mutex = new Semaphore(1);
	

	/*
	 * El Pasajero id quiere darse una vuelta en la barca desde la orilla pos
	 */
	public  void subir(int id,int pos) throws InterruptedException{
		if(pos == 1){
			puedoSubirBarcaNorte.acquire();
			System.out.println("Viajero "+id+" se sube al barco en la orilla "+pos);
			mutex.acquire();
			nPasajeros++;
			if(nPasajeros == 3){
				puedoIniciarViaje.release();
			} else {
				puedoSubirBarcaNorte.release();
			}
			mutex.release();
		} else {
			puedoSubirBarcaSur.acquire();
			System.out.println("Viajero "+id+" se sube al barco en la orilla "+pos);
			mutex.acquire();
			nPasajeros++;
			if(nPasajeros == 3){
				puedoIniciarViaje.release();
			} else {
				puedoSubirBarcaSur.release();
			}
			mutex.release();
		}
		
	}
	
	/*
	 * Cuando el viaje ha terminado, el Pasajero que esta en la barca se baja
	 */
	public  int bajar(int id) throws InterruptedException{
		puedoBajarBarca.acquire();
		mutex.acquire();
		System.out.println("Viajero "+id+" se ha bajado de la barca");
		nPasajeros--;
		if(nPasajeros > 0){
			puedoBajarBarca.release();
		} else {
			System.out.println("Barca vacía... pueden subir nuevos pasajeros");
			if(orilla == 1){
				puedoSubirBarcaNorte.release();
			} else {
				puedoSubirBarcaSur.release();
			}
		}
		mutex.release();

		return orilla;
	}
	/*
	 * El Capitan espera hasta que se suben 3 pasajeros para comenzar el viaje
	 */
	public  void esperoSuban() throws InterruptedException{
		puedoIniciarViaje.acquire();
		System.out.println("Empieza el viaje!!!");
		//TODO
	}
	/*
	 * El Capitan indica a los pasajeros que el viaje ha terminado y tienen que bajarse
	 */
	public  void finViaje() throws InterruptedException{
		System.out.println("Fin del viaje!!!");
		mutex.acquire();
		this.orilla = (orilla+1)%2;
		puedoBajarBarca.release();
		mutex.release();
	}

}
