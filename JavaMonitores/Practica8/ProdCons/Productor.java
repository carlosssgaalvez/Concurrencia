package ProdCons;
import java.util.Random;

public class Productor extends Thread{
	private Random r = new Random();
	private Buffer buffer;
	private int iter;
	private int id;
	public Productor(int id, Buffer b, int n) {
		buffer = b;
		iter = n;
		this.id = id;
	}
	
	public void run() {
		int dato;
		try {
			for (int i=0; i<iter; i++)
			{	
				dato = r.nextInt(100);
				buffer.producir(id, dato);	
			}		
		} catch (InterruptedException e) {			
			System.out.println("Productor interrumpido");
			e.printStackTrace();
		}
	}

}
