import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Convoy {
	private int tamañoConvoy;
	private int nFurgonetas;
	private int idLider;
	boolean destinoFinal;

	private Lock l;
	private Condition puedoCalcularRuta;
	private Condition puedoAbandonarConvoy;
	private Condition puedoAbandonarLider;
	
	public Convoy(int tam) {
		//TODO
		this.tamañoConvoy = tam;
		this.nFurgonetas = 0;
		this.destinoFinal = false;

		l = new ReentrantLock();
		puedoCalcularRuta = l.newCondition();
		puedoAbandonarConvoy = l.newCondition();
		puedoAbandonarLider = l.newCondition();
	}
	
	/**
	 * Las furgonetas se unen al convoy
	 * La primera es la lider, el resto son seguidoras 
	 **/
	public int unir(int id){
		//TODO: Poner los mensajes donde corresponda
		l.lock();
		try{
			this.nFurgonetas++;
			if(nFurgonetas == 1){
				System.out.println("** Furgoneta " +id + " lidera del convoy **");
				this.idLider = id;
			} else {
				System.out.println("Furgoneta "+id+" seguidora");
				if(nFurgonetas == tamañoConvoy) puedoCalcularRuta.signal();
			}
		} finally {
			l.unlock();
		}
		
		return idLider;
	}

	/**
	 * La furgoneta lider espera a que todas las furgonetas se unan al convoy 
	 * Cuando esto ocurre calcula la ruta y se pone en marcha
	 * @throws InterruptedException 
	 * */
	public void calcularRuta(int id) throws InterruptedException{
		//TODO
		l.lock();
		try{
			while(nFurgonetas < tamañoConvoy){
				puedoCalcularRuta.await();
			}
			System.out.println("** Furgoneta "+id+" lider:  ruta calculada, nos ponemos en marcha **");
		}finally{
			l.unlock();
		}
	}
	
	
	/** 
	 * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al destino y deben abandonar el convoy
	 * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
	 * @throws InterruptedException 
	 **/
	public void destino(int id) throws InterruptedException{
		//TODO
		l.lock();
		try{
			System.out.println("** Furgoneta "+ id +" líder: hemos llegado al destino **");
			destinoFinal = true;
			puedoAbandonarConvoy.signalAll();
			while(nFurgonetas > 1){
				puedoAbandonarLider.await();
			}
			nFurgonetas--;
			System.out.println("** Furgoneta "+id+" lider abandona el convoy **");
			destinoFinal = false;

		} finally {
			l.unlock();
		}

	}

	/**
	 * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al destino
	 * y abandonan el convoy
	 * @throws InterruptedException 
	 **/
	public void seguirLider(int id) throws InterruptedException{
		//TODO
		l.lock();
		try{
			while(!destinoFinal){
				puedoAbandonarConvoy.await();
			}
			System.out.println("Furgoneta "+id+" abandona el convoy");
			nFurgonetas--;
			if(nFurgonetas == 1){
				puedoAbandonarLider.signal();
			}
		} finally{
			l.unlock();
		}
	}

	
	
	/**
	* Programa principal. No modificar
	**/
	public static void main(String[] args) {
		final int NUM_FURGO = 10;
		Convoy c = new Convoy(NUM_FURGO);
		Furgoneta flota[ ]= new Furgoneta[NUM_FURGO];
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i] = new Furgoneta(i,c);
		
		for(int i = 0; i < NUM_FURGO; i++)
			flota[i].start();
	}

}
