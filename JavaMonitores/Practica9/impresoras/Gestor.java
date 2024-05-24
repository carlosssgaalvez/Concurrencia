
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Gestor {
	
	private int impADisponibles, impBDisponibles;
	private Lock l = new ReentrantLock();
	private Condition puedeImprimirA = l.newCondition();
	private Condition puedeImprimirB = l.newCondition();
	private Condition puedeImprimirAB = l.newCondition();

	public Gestor(int impA, int impB){
		impADisponibles = impA;
		impBDisponibles = impB;
	
	}
	public void imprimirA(int id) throws InterruptedException{
		l.lock();
		try {
			while(impADisponibles == 0) {
				puedeImprimirA.await();
			}
			System.out.println("DocumentoA "+id+" se imprime en A");
			impADisponibles--;
		}finally {
			l.unlock();
		}	
	}
	public void imprimirB(int id) throws InterruptedException {
		l.lock();
		try {
			while(impBDisponibles == 0) {
				puedeImprimirB.await();
			}
			System.out.println("DocumentoB "+id+" se imprime en B");
			impBDisponibles--;
		}finally {
			l.unlock();
		}	
	}
	public int imprimirAB(int id) throws InterruptedException {
		l.lock();
		try {
			int tipo = 0;
			while(impBDisponibles == 0 && impADisponibles == 0) {
				puedeImprimirAB.await();
			}
			if(impADisponibles > 0) {
				tipo = 0;
				System.out.println("DocumentoAB "+id+" se imprime en A");
				impADisponibles--;
			}else {
				tipo = 1;
				System.out.println("DocumentoAB "+id+" se imprime en B");
				impBDisponibles--;
			}
			return tipo;
		}finally {
			l.unlock();
		}	
	}
	public void liberar(int id, int tipo) { //tipo 0 => imp A || tipo 1=> impB
		l.lock();
		try {
			if(tipo == 0) {
				impADisponibles++;
				puedeImprimirA.signalAll();
			}else {
				impBDisponibles++;
				puedeImprimirB.signalAll();
			}
			puedeImprimirAB.signalAll();
		}finally {
			l.unlock();
		}	
	}

}
