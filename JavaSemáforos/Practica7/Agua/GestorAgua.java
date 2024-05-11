

import java.util.concurrent.Semaphore;

public class GestorAgua {
	private volatile int atomosH,atomosO, atomos;
	private Semaphore puedeEntrarOxigeno, puedeEntrarHidrogeno, moleculaFormada, mutex;
	
	
	public GestorAgua() {
		atomosH = 0;
		atomosO = 0;
		atomos = atomosH + atomosO;
		puedeEntrarOxigeno = new Semaphore(1);
		puedeEntrarHidrogeno = new Semaphore(1);
		mutex = new Semaphore(1);
		moleculaFormada = new Semaphore(0);
	}
	
	
	public void hListo(int id) throws InterruptedException {
		puedeEntrarHidrogeno.acquire();
		mutex.acquire();

		System.out.println("atomo de hidrogeno con id: " + id + "ha entrado en el gestor");
		atomosH++;
		System.out.println("atomosH: "+ atomosH);
		atomos++;
		System.out.println("atomos:" + atomos);

		if(atomos < 3){
			if(atomosH < 2){
				puedeEntrarHidrogeno.release();
			}
			mutex.release();
			moleculaFormada.acquire();
			mutex.acquire();
		} else {
			System.out.println("Molécula formada");
			atomosH = 0;
			atomosO = 0;
		}

		atomos--;

		if(atomos == 0){
			puedeEntrarHidrogeno.release();
			puedeEntrarOxigeno.release();
		} else {
			moleculaFormada.release();
		}

	}
	
	public void oListo(int id) throws InterruptedException {
		puedeEntrarOxigeno.acquire();			
	}
	
	public static void main(String[] args) {
		GestorAgua gestor = new GestorAgua();
		Oxigeno oxigenos[] = new Oxigeno[5];
		Hidrogeno hidrogenos[] = new Hidrogeno[5];
		for(int i = 0 ; i < 5; i++) {
			oxigenos[i] = new Oxigeno(i, gestor);
			hidrogenos[i] = new Hidrogeno(i, gestor);
		}

		for(int i = 0 ; i < 5; i++) {
			oxigenos[i].start();
			hidrogenos[i].start();
		}
	}

}
