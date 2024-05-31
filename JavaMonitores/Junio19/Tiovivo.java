import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Tiovivo {
	private int capacidad;
	private int nPasajeros;
	private boolean viajeEnCurso;
	private boolean subiendoPasajeros;
	private boolean bajandoPasajeros;

	private Lock l;
	private Condition puedoSubir;
	private Condition puedoIniciarViaje;
	private Condition puedoBajar;

	public Tiovivo(int c){
		this.capacidad = c;
		this.nPasajeros = 0;
		this.viajeEnCurso = false;
		this.subiendoPasajeros = true;
		this.bajandoPasajeros = false;

		l = new ReentrantLock();
		puedoSubir = l.newCondition();
		puedoBajar = l.newCondition();
		puedoIniciarViaje = l.newCondition();
	}
		
	
	public void subir(int id) throws InterruptedException 
	{	
		l.lock();
		try{
			while(nPasajeros == capacidad || bajandoPasajeros){
				puedoSubir.await();
			}
			nPasajeros++;
			System.out.println("Cliente: "+id+" se sube al tiovivo");
			if(nPasajeros == capacidad){
				viajeEnCurso = true;
				subiendoPasajeros = false;
				puedoIniciarViaje.signal();
			}


		} finally{
			l.unlock();
		}
		
	}
	
	public void bajar(int id) throws InterruptedException 
	{
		l.lock();
		try{
			while(viajeEnCurso || subiendoPasajeros){
				puedoBajar.await();
			}
			nPasajeros--;
			System.out.println("Cliente: "+id+" se baja al tiovivo");
			if(nPasajeros == 0){
				bajandoPasajeros = false;
				subiendoPasajeros = true;
				puedoSubir.signalAll();
			}
		} finally{
			l.unlock();
		}
	}
	
	public void esperaLleno () throws InterruptedException 
	{
		l.lock();
		try{
			while(nPasajeros < capacidad || subiendoPasajeros || bajandoPasajeros){
				puedoIniciarViaje.await();
			}
			System.out.println("Viaje iniciado");
		} finally{
			l.unlock();
		}			
	}
	
	public void finViaje () 
	{
		l.lock();
		try{
			System.out.println("Viaje finalizado");
			viajeEnCurso = false;
			bajandoPasajeros = true;
			puedoBajar.signalAll();
		} finally{
			l.unlock();
		}
	}
}
