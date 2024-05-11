package canibales;

public class Cocinero extends Thread {
	private Caldero cal;
	
	public Cocinero(Caldero c) {
		cal = c;
	}
	
	public void run() {
		try {while(!isInterrupted()) {
			cal.cocinar();
		}
		} catch (InterruptedException e) {
		}finally {
			System.out.println("Cocinero interrumpido");
		}
	}
}
