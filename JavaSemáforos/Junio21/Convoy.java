import java.util.concurrent.Semaphore;

public class Convoy {
	private int tamaño;
	private volatile int nFurgonetas;
	private int idLider;

	private Semaphore puedoCalcularRuta;
	private Semaphore puedoAbandonarConvoy;
	private Semaphore puedeAbandonarLider;
	private Semaphore mutex;
	
	public Convoy(int tam) {
		this.tamaño = tam;
		this.nFurgonetas = 0;
		puedoCalcularRuta = new Semaphore(0);
		puedoAbandonarConvoy = new Semaphore(0);
		puedeAbandonarLider = new Semaphore(0);
		mutex = new Semaphore(1);
	}
	
	/**
	 * Las furgonetas se unen al convoy
	 * La primera es la lider, el resto son seguidoras 
	 **/
	public int unir(int id) throws InterruptedException{
		//TODO: Poner los mensajes donde corresponda
		mutex.acquire();
		nFurgonetas++;
		if(nFurgonetas == 1){
			System.out.println("** Furgoneta " +id + " lidera del convoy **");
			idLider = id;
		} else {
			System.out.println("Furgoneta "+id+" seguidora");
			if(nFurgonetas == tamaño){
				puedoCalcularRuta.release();
			}
		}
		mutex.release();

		return idLider;
	}

	/**
	 * La furgoneta lider espera a que todas las furgonetas se unan al convoy 
	 * Cuando esto ocurre calcula la ruta y se pone en marcha
	 * */
	public void calcularRuta(int id) throws InterruptedException{
		//TODO
		puedoCalcularRuta.acquire();
		System.out.println("** Furgoneta "+id+" lider:  ruta calculada, nos ponemos en marcha **");
	}
	
	
	/** 
	 * La furgoneta lider avisa al las furgonetas seguidoras que han llegado al destino y deben abandonar el convoy
	 * La furgoneta lider espera a que todas las furgonetas abandonen el convoy
	 **/
	public void destino(int id) throws InterruptedException{
		//TODO
		System.out.println("** Furgoneta "+id+" hemos llegado a destino **");
		puedoAbandonarConvoy.release();
		puedeAbandonarLider.acquire();
		System.out.println("** Furgoneta "+id+" lider abandona el convoy **");
	}

	/**
	 * Las furgonetas seguidoras hasta que la lider avisa de que han llegado al destino
	 * y abandonan el convoy
	 **/
	public void seguirLider(int id) throws InterruptedException{
		//TODO
		puedoAbandonarConvoy.acquire();
		mutex.acquire();
		System.out.println("Furgoneta "+id+" abandona el convoy");
		nFurgonetas--;
		if(nFurgonetas == 1){
			puedeAbandonarLider.release();
		} else {
			puedoAbandonarConvoy.release();
		}
		mutex.release();
		
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
