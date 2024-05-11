package sensores;


import java.util.concurrent.Semaphore;

public class Sistema {
	private int medidas[];	
	private int nMedidas;
	private Semaphore mutex;
	private Semaphore puedoProcesar;
	private Semaphore[] puedoMedir;
	//TODO
	
	public Sistema() {
		medidas = new int[3];
		nMedidas = 0;
		mutex = new Semaphore(1);
		puedoProcesar = new Semaphore(0);
		puedoMedir = new Semaphore[3];
		for(int i = 0; i < 3; i++){
			puedoMedir[i] = new Semaphore(1);
		}
		//TODO
	}
	
	public void ponerMedida(int id, int dato) throws InterruptedException {
		//TODO
		puedoMedir[id].acquire();
		mutex.acquire();

		nMedidas++;
		medidas[id] = dato;

		if(nMedidas == 3){
			puedoProcesar.release();
		}
		
		mutex.release();

	}
	
	public void procesarMedidas() throws InterruptedException {
		//TODO
		puedoProcesar.acquire();		
		System.out.println( "Luz "+ medidas[0]  +" Hum "+medidas[1] +" Temp "+medidas[2]);

		mutex.acquire();

		nMedidas = 0;

		mutex.release();
		for(int i = 0; i < 3; i++){
			puedoMedir[i].release();
		}	
	}




	 public static void main(String[] args) {
		 Sistema s = new Sistema();
			Trabajador t1 = new Trabajador(s);
			Sensor sensors[] = new Sensor[3];
			for(int i = 0; i < 3; i++)
				sensors[i] = new Sensor(i, s);
			
			t1.start();
			for(int i = 0; i < 3; i++)
				sensors[i].start();
			
			
			//Vamos a dormir un rato y luego interrumpimos todas las hebras
			try {
				Thread.sleep(2000);
				for(int i = 0; i < 3; i++)
					sensors[i].interrupt();
				t1.interrupt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	
}

 