import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Barca {
	private int posBarca = 1;
	private int nPasajeros = 0;
	private final int CAPACIDAD_BARCA = 3;
	private boolean viajeEnCurso = false;
	private boolean nuevosPasajerosSubidos = false;
	private boolean subiendoPasajeros = true;
	private boolean bajandoPasajeros = false;

	private Lock l = new ReentrantLock();
	private Condition puedoSubirNorte = l.newCondition();
	private Condition puedoSubirSur = l.newCondition();
	private Condition puedoIniciarViaje = l.newCondition();
	private Condition puedoBajar = l.newCondition();

	

	/*
	 * El Pasajero id quiere darse una vuelta en la barca desde la orilla pos
	 */
	public  void subir(int id,int pos) throws InterruptedException{
		//TODO
		l.lock();
		try{
			if(pos == 0){ // sur
				while(pos != posBarca || nPasajeros == CAPACIDAD_BARCA || bajandoPasajeros){
					puedoSubirSur.await();
				}
			} else { // norte
				while(pos != posBarca || nPasajeros == CAPACIDAD_BARCA || bajandoPasajeros){
					puedoSubirNorte.await();
				}
			}
			subiendoPasajeros = true;
	
			nPasajeros++;
			System.out.println("Viajero "+id+" se sube a la barca en la orilla "+pos);
			if(nPasajeros == CAPACIDAD_BARCA){
				viajeEnCurso = true;
				subiendoPasajeros = false;
				nuevosPasajerosSubidos = true;
				puedoIniciarViaje.signal();
			}
			
		} finally{
			l.unlock();
		}
	}
	
	/*
	 * Cuando el viaje ha terminado, el Pasajero que esta en la barca se baja
	 */
	public  int bajar(int id) throws InterruptedException{
		//TODO
		l.lock();
		try{
			while(viajeEnCurso || subiendoPasajeros){
				puedoBajar.await();
			}
			bajandoPasajeros = true;
			nPasajeros--;
			System.out.println("Viajero "+id+" se baja de la barca");
			if(nPasajeros == 0){
				bajandoPasajeros = false;
				if(posBarca == 0){
					puedoSubirSur.signalAll();
				} else {
					puedoSubirNorte.signalAll();
				}
			}


		} finally{
			l.unlock();
		}
		return posBarca;
	}
	/*
	 * El Capitan espera hasta que se suben 3 pasajeros para comenzar el viaje
	 */
	public  void esperoSuban() throws InterruptedException{
		//TODO
		l.lock();
		try{
			while(nPasajeros < CAPACIDAD_BARCA || !nuevosPasajerosSubidos){
				puedoIniciarViaje.await();
			}
			System.out.println("Empieza el viaje!!!!");

		} finally{
			l.unlock();
		}
	}
	/*
	 * El Capitan indica a los pasajeros que el viaje ha terminado y tienen que bajarse
	 */
	public  void finViaje() throws InterruptedException{
		//TODO
		l.lock();
		try{
			posBarca = (posBarca+1)%2;
			System.out.println("Fin del viaje!!!!");
			viajeEnCurso = false;
			nuevosPasajerosSubidos = false;
			puedoBajar.signalAll();
		} finally{
			l.unlock();
		}
	}

}
