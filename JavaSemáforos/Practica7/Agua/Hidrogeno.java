

public class Hidrogeno extends Thread {
	private int id;
	private GestorAgua gestor;
	
	public Hidrogeno(int id, GestorAgua g) {
		this.id = id;
		gestor = g;
	}
	
	public void run() {
		
		try {
			while(!this.isInterrupted()) {
				Thread.sleep(100);
				gestor.hListo(id);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
