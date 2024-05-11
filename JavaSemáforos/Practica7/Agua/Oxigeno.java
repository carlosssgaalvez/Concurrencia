

public class Oxigeno extends Thread {
	private int id;
	private GestorAgua gestor;
	
	public Oxigeno(int id, GestorAgua g) {
		this.id = id;
		gestor = g;
	}
	
	public void run() {
		
			try {
				while(!this.isInterrupted()) {
				Thread.sleep(100);
				gestor.oListo(id);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}
	
}
