import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Curso {

	//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int MAX_ALUMNOS_INI = 10;
	
	//Numero de alumnos por grupo en la parte avanzada
	private final int ALUMNOS_AV = 3;

	private int nAlumnosIni = 0;
	private int nAlumnosAv = 0;
	private boolean trabajoAvanzadoEnCurso = false;

	private Lock l = new ReentrantLock();
	private Condition puedoHacerIniciacion = l.newCondition();
	private Condition puedoHacerAvanzado = l.newCondition();
	private Condition tenemosGrupoAvanzado = l.newCondition();
	private Condition abandonaGrupoAvanzado = l.newCondition();
	
	
	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		l.lock();
		try{
			while(nAlumnosIni == MAX_ALUMNOS_INI){
				puedoHacerIniciacion.await();
			}

			nAlumnosIni++;
			System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");

		} finally {
			l.unlock();
		}
	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		l.lock();
		try{
			System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
			nAlumnosIni--;
			puedoHacerIniciacion.signalAll();
		} finally {
			l.unlock();
		}
	}
	
	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		l.lock();
		try{
			while(trabajoAvanzadoEnCurso){
				puedoHacerAvanzado.await();
			}
			nAlumnosAv++;
			if(nAlumnosAv < ALUMNOS_AV){
				System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");
				while(!trabajoAvanzadoEnCurso){
					tenemosGrupoAvanzado.await();
				}
			} else {
				System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
				trabajoAvanzadoEnCurso = true;
				tenemosGrupoAvanzado.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
	
	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada 
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte 
	 */ 
	public void finAvanzado(int id) throws InterruptedException{
		//Espera a que los 3 alumnos terminen su parte avanzada
		l.lock();
		try{
			nAlumnosAv--;
			if(nAlumnosAv > 0){
				System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
				while(nAlumnosAv > 0){
					abandonaGrupoAvanzado.await();
				}

			} else {
				System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
				abandonaGrupoAvanzado.signalAll();
				trabajoAvanzadoEnCurso = false;
				puedoHacerAvanzado.signalAll();
			}
		} finally{
			l.unlock();
		}
	}
}
