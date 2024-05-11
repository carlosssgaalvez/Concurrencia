

import java.util.concurrent.Semaphore;

public class Curso {

	//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int MAX_ALUMNOS_INI = 10;
	
	//Numero de alumnos por grupo en la parte avanzada
	private final int ALUMNOS_AV = 3;

	private volatile int nAlumnosAvanzados = 0;
	private volatile int nAlumnosInicidos = 0;
	private Semaphore mutexIniciacion = new Semaphore(1); 
	private Semaphore mutexAvanzado = new Semaphore(1);
	private Semaphore entroIniciacion = new Semaphore(1);
	private Semaphore entroAvanzado = new Semaphore(1);
	private Semaphore puedoHacerAvanzado = new Semaphore(0);
	private Semaphore puedoSalirAvanzado = new Semaphore(0);

	
	
	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		//Espera si ya hay 10 alumnos cursando esta parte
		entroIniciacion.acquire();

		//Mensaje a mostrar cuando el alumno pueda conectarse y cursar la parte de iniciacion
		System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");

		mutexIniciacion.acquire();
		nAlumnosInicidos++;
		if(nAlumnosInicidos < MAX_ALUMNOS_INI){
			entroIniciacion.release();
		}
		mutexIniciacion.release();
	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
		System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");

		//Libera la conexion para que otro alumno pueda usarla
		mutexIniciacion.acquire();
		nAlumnosInicidos--;
		if(nAlumnosInicidos == MAX_ALUMNOS_INI-1){
			entroIniciacion.release();
		}
		mutexIniciacion.release();
	}
	
	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		//Espera a que no haya otro grupo realizando esta parte
		entroAvanzado.acquire();

		mutexAvanzado.acquire();
		nAlumnosAvanzados++;
		if(nAlumnosAvanzados < ALUMNOS_AV){
			System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");
			entroAvanzado.release();
		} else {
			System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
			puedoHacerAvanzado.release();
		}
		mutexAvanzado.release();
		puedoHacerAvanzado.acquire();
	
	}
	
	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada 
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte 
	 */ 
	public void finAvanzado(int id) throws InterruptedException{
		//Espera a que los 3 alumnos terminen su parte avanzada

		//puedoSalirAvanzado.acquire();
		mutexAvanzado.acquire();
		nAlumnosAvanzados--;
		if(nAlumnosAvanzados > 0){
			System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
			puedoHacerAvanzado.release();
		} else {
			System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
			puedoSalirAvanzado.release();
		}
		mutexAvanzado.release();

		puedoSalirAvanzado.acquire();
		mutexAvanzado.acquire();
		nAlumnosAvanzados++;
		if(nAlumnosAvanzados < ALUMNOS_AV){
			puedoSalirAvanzado.release();
		} else{
			nAlumnosAvanzados = 0;
			entroAvanzado.release();
		}
		mutexAvanzado.release();
		
	}
}
