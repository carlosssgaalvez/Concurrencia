package Recursos;
import java.util.Random;

public class Proceso extends Thread{
	private Control c;
	private int id;
	private Random r;
	private int max;
	private int rec;
	public Proceso(int id, Control c, int max) {
		this.c = c;
		this.id = id;
		this.max = max; //num maximo recursos que se pueden solicitar
		r = new Random();
	}
	
	public void run() {
		
		try {
			while(!this.isInterrupted()) {
				rec = 1+ r.nextInt( max);
				c.quieroRecursos(id, rec);
				Thread.sleep(r.nextInt(100));
				c.liberoRecursos(id, rec);
			}
		}catch(InterruptedException e) {}
	}
}
