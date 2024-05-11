
import java.util.concurrent.Semaphore;

public class SupermercadoSemaforos implements Supermercado {

	
	
	private Cajero permanente;
	private volatile int clientesEsperando = 0;
	private volatile boolean supermercadoAbierto = true;

	private Semaphore puedoSerAtendido = new Semaphore(1);
	private Semaphore puedoAtenderCliente = new Semaphore(0);
	private Semaphore mutexClientes = new Semaphore(1);
	private Semaphore mutexSupermercado = new Semaphore(1);

	
	
	public SupermercadoSemaforos() throws InterruptedException {
		permanente = new Cajero(this, true); //crea el primer cajero, el permanente
		permanente.start();		
		//TODO
	}

	//No debe admitir mas clientes
	@Override
	public void fin() throws InterruptedException {
		mutexSupermercado.acquire();
		supermercadoAbierto = false;
		mutexSupermercado.release();
	}

	@Override
	public void nuevoCliente(int id) throws InterruptedException {
		mutexSupermercado.acquire();
		if(supermercadoAbierto){ // entra en el super
			mutexSupermercado.release();

			mutexClientes.acquire();
			clientesEsperando++;
			System.out.println("Llega cliente "+ id +". Hay "+ clientesEsperando);
			if(clientesEsperando == 1){ // soy el primero
				puedoAtenderCliente.release();
				mutexClientes.release();
				puedoSerAtendido.acquire();
			} else {
				if(clientesEsperando > 3 * Cajero.numCajeros()){
					Cajero cOcasional = new Cajero(this, false);
					cOcasional.run();
				}
				mutexClientes.release();
				puedoSerAtendido.acquire();
			}

		} else{ // el super esta cerrado por tanto se va
			mutexSupermercado.release();
		}
		
	}

	@Override
	public boolean permanenteAtiendeCliente( int id) throws InterruptedException {
		boolean heAtendido = true;

		mutexClientes.acquire();
		mutexSupermercado.acquire();
		if(clientesEsperando == 0 && !supermercadoAbierto){ // super cerrado y sin clientes
			heAtendido = false;
			mutexSupermercado.release();
			mutexClientes.release();
		} else {		// super abierto
			mutexSupermercado.release();
			if(clientesEsperando > 0){
				clientesEsperando--;
				System.out.println("Cajero permanente atiende a un cliente. Quedan "+clientesEsperando);
				puedoSerAtendido.release();
				mutexClientes.release();
			} else {
				System.out.println("Cajero permanente espera");
				mutexClientes.release();

				puedoAtenderCliente.acquire();
				mutexClientes.acquire();
				clientesEsperando--;
				System.out.println("Cajero permanente atiende a un cliente. Quedan "+clientesEsperando);
				puedoSerAtendido.release();
				mutexClientes.release();
			}
			
		}
			
		return heAtendido;
		
	}
		
	
	@Override
	public boolean ocasionalAtiendeCliente(int id) throws InterruptedException {
		boolean heAtendido = true;
	
		mutexClientes.acquire();
		if(clientesEsperando == 0){
			heAtendido = false;
			mutexClientes.release();
		} else {
			System.out.println("Cajero "+(Cajero.numCajeros()-1)+" atiende a "+id);
			clientesEsperando--;
			puedoSerAtendido.release();
			mutexClientes.release();
		}

		return heAtendido;//borrar
	}

}
