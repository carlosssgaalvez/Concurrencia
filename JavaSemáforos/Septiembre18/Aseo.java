import java.util.concurrent.Semaphore;

public class Aseo {
	private volatile int nHombre = 0;
	private volatile int nMujeres = 0;

	private Semaphore puedeEntrarMujer = new Semaphore(0);
	private Semaphore puedeEntrarHombre = new Semaphore(1);
	private Semaphore mutex = new Semaphore(1);

	
	/**
	 * El hombre id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay alguna mujer en ese
	 * momento en el aseo
	 */
	public void llegaHombre(int id) throws InterruptedException{
		mutex.acquire();
		puedeEntrarHombre.acquire();
		mutex.acquire();
		nHombre++;
		System.out.println("Ha entrado un hombre en el aseo. Hay "+nHombre+" hombre/s" );
		puedeEntrarHombre.release();
		mutex.release();
	}
	/**
	 * La mujer id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay algun hombre en ese
	 * momento en el aseo
	 */
	public void llegaMujer(int id) throws InterruptedException{
		puedeEntrarMujer.acquire();
		mutex.acquire();
		nMujeres++;
		System.out.println("Ha entrado una mujer en el aseo. Hay "+nMujeres+" mujer/es" );
		puedeEntrarMujer.release();
		mutex.release();
	}
	/**
	 * El hombre id, que estaba en el aseo, sale
	 */
	public void saleHombre(int id)throws InterruptedException{
		mutex.acquire();
		nHombre--;
		System.out.println("Ha salido un hombre en el aseo. Hay "+nHombre+" hombre/s" );
		if(nHombre == 0){
			puedeEntrarMujer.release();
		}
		mutex.release();
	}
	
	public void saleMujer(int id)throws InterruptedException{
		mutex.acquire();
		nMujeres--;
		System.out.println("Ha salido una mujer en el aseo. Hay "+nMujeres+" mujer/es" );
		if(nMujeres == 0){
			puedeEntrarHombre.release();
		}
		mutex.release();
	}
}
