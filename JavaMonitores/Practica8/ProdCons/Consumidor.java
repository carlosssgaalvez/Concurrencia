package ProdCons;

public class Consumidor extends Thread {

	private Buffer buffer;
	private int iter;
	private int id;
	public Consumidor(int id, Buffer b, int n) {
		buffer = b;
		iter = n;
		this.id = id;
	}
	
	public void run()
	{
		try {
			for(int i=0; i<iter; i++)
			{
				buffer.consumir(id);
			}
		} catch (InterruptedException e) {
			System.out.println("Consumidor interrumpido");
			e.printStackTrace();
		}


	}
}
