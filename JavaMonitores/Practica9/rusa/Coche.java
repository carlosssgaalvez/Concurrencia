import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Coche {
	private int capacidad = 4;
	private int pasajeros;

	private Lock l;
	private Condition puedeSubir;
	private Condition puedeBajar;
	private Condition puedeIniciarViaje;


	public Coche() {
		pasajeros = 0;
		l = new ReentrantLock();
		puedeSubir = l.newCondition();
		puedeBajar = l.newCondition();
		puedeIniciarViaje = l.newCondition();
	}
	public void quieroSubir(int id) throws InterruptedException {
		l.lock();
		try {
			while (pasajeros == capacidad) {
				puedeSubir.await();
			}
			pasajeros++;
			System.out.println("Pasajero " + id + " sube al coche");
			if (pasajeros == capacidad) {
				puedeIniciarViaje.signal();
			}
		} finally {
			l.unlock();
		}
	}

	public void quieroBajar(int id) throws InterruptedException {
		l.lock();
		try {
			puedeBajar.await();
			pasajeros--;
			System.out.println("Pasajero " + id + " baja del coche");
			if (pasajeros == 0) {
				puedeSubir.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
	
	public void inicioViaje() throws InterruptedException {		
		l.lock();
		try {
			puedeIniciarViaje.await();
			System.out.println("Atraccion iniciando viaje");
		} finally {
			l.unlock();
		}
	}
	
	public void finViaje() throws InterruptedException {							
		l.lock();
		try {
			System.out.println("Atraccion finalizando viaje");
			puedeBajar.signalAll();
		} finally {
			l.unlock();
		}
	}
}
