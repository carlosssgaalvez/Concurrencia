import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {
	
	private int capacidadVagon = 5;
	private int pasajerosVagon1 = 0;
	private int pasajerosVagon2 = 0;
	private boolean vagonVacio = true;
	private boolean viajeFinalizado = false;
	private boolean puedoIniciarViaje = false;


	private Lock l = new ReentrantLock();
	private Condition puedeIniciarViaje = l.newCondition();
	private Condition puedeSubirPasajero = l.newCondition();;
	private Condition puedeBajarVagon1 = l.newCondition();;
	private Condition puedeBajarVagon2 = l.newCondition();;

	
	public void viaje(int id) throws InterruptedException {
		l.lock();
		try{
			while(!vagonVacio){
				puedeSubirPasajero.await();
			}
			if(pasajerosVagon1 < capacidadVagon){
				pasajerosVagon1++;
				System.out.println("El pasajero " + id + " se ha sentado en el vagon 1");
				while(!viajeFinalizado){
					puedeBajarVagon1.await();
				}
				pasajerosVagon1--;
				System.out.println("El pasajero " + id + " se ha bajado del vagon 1");
				if(pasajerosVagon1 == 0){
					puedeBajarVagon2.signalAll();
				}
			}else {
				pasajerosVagon2++;
				System.out.println("El pasajero " + id + " se ha sentado en el vagon 2");
				if(pasajerosVagon2 == capacidadVagon){
					vagonVacio = false;
					puedoIniciarViaje = true;
					puedeIniciarViaje.signal();
				}
				while(pasajerosVagon1 != 0){
					puedeBajarVagon2.await();
				}
				pasajerosVagon2--;
				System.out.println("El pasajero " + id + " se ha bajado del vagon 2");
				if(pasajerosVagon2 == 0){
					vagonVacio = true;
					viajeFinalizado = false;
					puedeSubirPasajero.signalAll();
				}
			}
			
		} finally {
			l.unlock();
		}
			
	}

	public void empiezaViaje() throws InterruptedException {
		l.lock();
		try{
			while(!puedoIniciarViaje){
				puedeIniciarViaje.await();
			}
			puedoIniciarViaje = false;
			System.out.println("        Maquinista:  empieza el viaje");
		} finally {
			l.unlock();
		}
		
	}
	public void finViaje() throws InterruptedException  {
		l.lock();
		try{
			viajeFinalizado = true;
			System.out.println("        Maquinista:  fin del viaje");
			puedeBajarVagon1.signalAll();
		} finally {
			l.unlock();
		}
	}
}
