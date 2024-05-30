import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DatoCompartido {
	private int dato; //Dato a procesar
	private int nProcesadores; //Numero de procesadores totales
	private int procPend; //Numero de procesadores pendientes de procesar el dato
	private boolean datoProcesando;

	private Lock l;
	private Condition puedoMostrarDatoProcesado;
	private Condition puedoProcesarDato;

	
	
	/* Recibe como par�metro el n�mero de procesadores que tienen que manipular 
	 * cada dato generado. Debe ser un n�mero mayor que 0. 
	 */
	public DatoCompartido(int nProcesadores) {
		if(nProcesadores > 0){
			this.nProcesadores = nProcesadores;	
		} else {
			this.nProcesadores = 0;
		}

		datoProcesando = false;
		procPend = 0;
		l = new ReentrantLock();
		puedoMostrarDatoProcesado = l.newCondition();
		puedoProcesarDato = l.newCondition();
	}
	
	/*  El Generador utiliza este metodo para almacenar un nuevo dato a procesar. 
	 *  Una vez almacenado el dato se debe avisar a los procesadores de que se ha 
	 *  almacenado un nuevo dato. 
	 *  
	 *  Por ultimo, el Generador tendra que esperar en este metodo a que todos los 
	 *  procesadores terminen de procesar el dato. Una vez que todos terminen, 
	 *  se devolvera el resultado del procesamiento, permitiendo al Generador 
	 *  la generacion de un nuevo dato.
	 * 
	 *  CS_Generador: Una vez generado un dato, el Generador espera a que todos los procesadores 
	 *  terminen antes de generar el siguiente dato
	 */
	public int generaDato(int d) throws InterruptedException {
		//COMPLETAR y colocar los mensajes en el lugar apropiado dentro del c�digo
		l.lock();
		try{
			this.dato = d;
			System.out.println("Dato a procesar: " + dato);
			this.procPend = this.nProcesadores;
			System.out.println("Numero de procesadores pendientes: " + procPend);
			puedoProcesarDato.signal();
			while(procPend != 0){
				puedoMostrarDatoProcesado.await();
			}

		} finally {
			l.unlock();
		}
		return dato;
	}

	/*  El Procesador con identificador id utiliza este metodo para leer el 
	 *  dato que debe procesar (el dato se devuelve como valor de retorno del metodo). 
	 *  Debera esperarse si no hay datos nuevos para procesar 
	 *  o si otro procesador esta manipulando el dato. 
	 * 
	 *  CS1_Procesador: Espera si no hay un nuevo dato que procesar. 
	 *                  Esto puede ocurrir porque el generador aun no haya almacenado 
	 *                  ningun dato o porque el Procesador ya haya procesado el dato 
	 *                  almacenado en ese momento en el recurso compartido.
	 *  CS2_Procesador: Espera a que el dato este disponible para poder procesarlo 
	 *                  (es decir, no hay otro Procesador procesando al dato)
	 */
	public int leeDato(int id) throws InterruptedException {
		//COMPLETAR
		l.lock();
		try{
			while(procPend == 0 || datoProcesando){
				puedoProcesarDato.await();
			}
			datoProcesando = true;
		} finally {
			l.unlock();
		}
		return dato;
	}
	
	/*  El Procesador con identificador id almacena en el recurso compartido el resultado 
	 *  de haber procesado el dato. Una vez hecho esto actuara de una de las dos formas siguientes: 
	 *  
	 *  (1) Si aun hay procesadores esperando a procesar el dato los avisara, 
	 *  (2) Si el era el ultimo procesador avisara al Generador de que han terminado. 
	 * 
	 */
	public void actualizaDato(int id, int datoActualizado) {
		//COMPLETAR y colocar los mensajes en el lugar apropiado dentro del c�digo
		l.lock();
		try{
			this.dato = datoActualizado;
			System.out.println("	Procesador " + id + " ha procesado el dato. Nuevo dato: " + dato);
			procPend--;
			System.out.println("Numero de procesadores pendientes: " + procPend);
			datoProcesando = false;
			if(procPend > 0){
				puedoProcesarDato.signal();
			} else {
				puedoMostrarDatoProcesado.signal();
			}

		}finally{
			l.unlock();
		}

	}
}
