package Fumadores;

public class Mesa {
	private int ingrediente;
	
	public Mesa() {
		ingrediente = -1;		
		//TODO		
	}

	public  synchronized void quiereFumar(int id)  throws InterruptedException{
		while(ingrediente != id){
			wait();
		}
		System.out.println("El fumador " + id + " esta fumando");
	}

	public synchronized void terminaFumar(int id) {
		System.out.println("El fumador " + id + " ha terminado de fumar");
		ingrediente = -1;
		notifyAll();		
	}

	public synchronized void poneIngrediente(int ing) throws InterruptedException {
		while(ingrediente != -1){ //Si hay ingredientes en la mesa no ponemos
			wait();
		}
		System.out.println("El agente pone ingredientes. Falta ingrediente :" + ing);
		ingrediente = ing;
		notifyAll();
	}

	public static void main(String[] args) {
		Mesa m = new Mesa();
		Agente ag = new Agente(m);
		Fumador  fumadores[] = new Fumador[3];  // 0 - Tabaco || 1 - Papel || 2 - Cerilla
		for(int i = 0 ; i < 3; i++) 
			fumadores[i] = new Fumador(i, m);
		ag.start();
		for(int i = 0 ; i < 3; i++) 
			fumadores[i].start();
		
	}

}
