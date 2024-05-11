
import java.util.concurrent.Semaphore;

public class Nido {
	private final int maxBichos;
	private volatile int bichitos;

	private Semaphore puedeComer, puedePoner,mutex;
	
	
	public Nido(int max) {
		maxBichos = max;
		bichitos = 0;
		puedePoner = new Semaphore(1);
		puedeComer = new Semaphore(0);
		mutex = new Semaphore(1);
	}
	
	public void depositarBicho(int id) throws InterruptedException {
		puedePoner.acquire();
		mutex.acquire();
		System.out.println("Padre " + id + " ha depositado un bichito");
		bichitos++;
		System.out.println("bichitos: " + bichitos);
		if(bichitos < maxBichos){
			puedePoner.release();
		}
		if(bichitos == 1){
			puedeComer.release();
		}
		mutex.release();
	}

	public void comerBicho(int id) throws InterruptedException {
		puedeComer.acquire();
		mutex.acquire();

		System.out.println("Polluelo "+ id + " ha comido un bichito");
		bichitos--;
		System.out.println("bichitos: " + bichitos);
		if(bichitos > 0){
			puedeComer.release();
		}
		if(bichitos == maxBichos-1){
			puedePoner.release();
		}

		mutex.release();
	}
	
	public static void main(String[] args) {
		Nido nido = new Nido(7);
		Pajaro p0 = new Pajaro(0,nido);
		Pajaro p1 = new Pajaro(1, nido);
		Polluelo polluelos[] = new Polluelo[5];
		for(int i = 0; i < 5; i++)
			polluelos[i] = new Polluelo(i, nido);
		p0.start();
		p1.start();
		for(int i = 0; i < 5; i++)
			polluelos[i] .start();
		
		
		try {
			Thread.sleep(2000);
			p0.interrupt();
			p1.interrupt();
			for(int i = 0; i < 5; i++)
				polluelos[i].interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}

