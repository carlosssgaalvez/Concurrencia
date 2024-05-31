import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Parada {
	public boolean busEnParada = false;
	private int pasajerosSuben;
	private int pasajerosEsperan;
	
	private ReentrantLock l;
	private Condition pasajerosVanASubir;
	private Condition pasajerosVanAEsperar;
	
	
	public Parada(){
		busEnParada = false;
		pasajerosSuben = 0;
		pasajerosEsperan = 0;
		l = new ReentrantLock();
		pasajerosVanASubir = l.newCondition();
		pasajerosVanAEsperar = l.newCondition();
	}
	/**
	 * El pasajero id llama a este metodo cuando llega a la parada.
	 * Siempre tiene que esperar el siguiente autobus en uno de los
	 * dos grupos de personas que hay en la parada
	 * El metodo devuelve el grupo en el que esta esperando el pasajero
	 * 
	 */
	public int esperoBus(int id) throws InterruptedException{
		l.lock();
		try{
			System.out.println("Pasajero: "+id+" espera al bus");
			if(busEnParada){
				pasajerosEsperan++;
				return 1;
			} else {
				pasajerosSuben++;
				return 0;
			}
		} finally{
			l.unlock();
		}
	}
	/**
	 * Una vez que ha llegado el autobús, el pasajero id que estaba
	 * esperando en el grupo i se sube al autobus
	 * @throws InterruptedException 
	 *
	 */
	public void subeAutobus(int id,int i) throws InterruptedException{
		l.lock();
		try{
			if(i == 1){
				pasajerosVanAEsperar.await();
			}
			while(!busEnParada){
				pasajerosVanASubir.await();
			}
			System.out.println("Pasajero: "+id+" se sube al bus");
			pasajerosSuben--;
			if(pasajerosSuben == 0){
				busEnParada = false;
				pasajerosVanAEsperar.signalAll();
				pasajerosSuben = pasajerosEsperan;
				pasajerosEsperan = 0;
			}
		} finally{
			l.unlock();
		}
	}
	/**
	 * El autobus llama a este metodo cuando llega a la parada
	 * Espera a que se suban todos los viajeros que han llegado antes
	 * que el, y se va
	 * 
	 */
	public void llegoParada() throws InterruptedException{
		l.lock();
		try{
			busEnParada = true;
			System.out.println("Ha llegado el bus");
			pasajerosVanASubir.signalAll();
		} finally{
			l.unlock();
		}
	}
}
