package canibales;
import java.util.concurrent.Semaphore;

public class Caldero {
	private int raciones, maxRaciones;
	Semaphore puedoCocinar;
	Semaphore puedoComer;
	Semaphore puedoMirarRaciones;
	Semaphore mutex;
	//TODO 

	public Caldero(int max) {
		raciones = 0;
		maxRaciones = max;
		puedoCocinar = new Semaphore(0);
		puedoComer = new Semaphore(0);
		puedoMirarRaciones = new Semaphore(1);
		mutex = new Semaphore(1);
		
		//TODO
	}
	public void comer(int id) throws InterruptedException {
		//TODO 
		
		puedoMirarRaciones.acquire();
		mutex.acquire();
		if(raciones == 0){
			System.out.println("Canibal "+id+" avisa al cocinero");
			mutex.release();
			puedoCocinar.release();
		} else {
			mutex.release();
			puedoComer.release();
		}
		

		puedoComer.acquire();
		mutex.acquire();
		System.out.println("Canibal "+id+ " come");
		raciones--;
		mutex.release();	
		puedoMirarRaciones.release();
	}

	public void cocinar() throws InterruptedException {
		//TODO 
		puedoCocinar.acquire();
		mutex.acquire();

		System.out.println("Cocinero prepara 10 raciones");

		raciones = maxRaciones;

		mutex.release();
		puedoComer.release();	
		
	}

	public static void main(String[] args) {
		Caldero caldero = new Caldero(10);
		Cocinero co = new Cocinero(caldero);
		Canibal can[] = new Canibal[6];
		for(int i=0; i < 6; i++)
			can[i] = new Canibal(i, caldero);
		
		co.start();
		for(int i=0; i < 6; i++)
			can[i].start();
		
		try {
			Thread.sleep(2500);
			co.interrupt();
			for(int i=0; i < 6; i++)
				can[i].interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	

}
/**/