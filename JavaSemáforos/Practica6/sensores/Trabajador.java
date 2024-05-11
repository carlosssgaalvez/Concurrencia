package sensores;


public class Trabajador extends Thread {
	private Sistema sis;
	
	public Trabajador(Sistema s) {
		sis = s;
	}
	
	public void run() {
		try {
			while(!this.isInterrupted()) {
				sis.procesarMedidas();
			}
		} catch (InterruptedException e) {
			
		}finally {
			System.out.println("Trabajador interrumpido");
		}			

	}
	
}
