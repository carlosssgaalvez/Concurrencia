import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GestorAgua {	

	private int atomosH = 0;
	private boolean atomosO = false;

	ReentrantLock l = new ReentrantLock();
	Condition puedeEntrarH = l.newCondition();
	Condition puedeEntrarO = l.newCondition();
	Condition moleculaFormada = l.newCondition();

	public void hListo(int id) throws InterruptedException {
		//ENTRA UN ATOMO DE HIDROGENO
		l.lock();
		try{
			while(atomosH == 2){
				puedeEntrarH.await();
			}
			System.out.println("Atomo de Hidrogeno "+id+" ha entrado");
			atomosH++;
			if(atomosH == 2 && atomosO){
				moleculaFormada.signalAll();
				System.out.println("Molecula de agua formada");
			} else { // molecula no formada
				moleculaFormada.await();
			}

			System.out.println("Atomo de Hidrogeno "+id+" ha salido");
			atomosH--;

			if(atomosH == 0){
				puedeEntrarH.signalAll();
			}

		} finally{
			l.unlock();
		}
	}
	
	public void oListo(int id) throws InterruptedException { 
		//ENTRA UN ATOMO DE OXIGENO
		l.lock();
		try{
			while(atomosO){
				puedeEntrarO.await();
			}
			System.out.println("Atomo de Oxigeno "+id+" ha entrado");
			atomosO = true;
			if(atomosH == 2 && atomosO){
				moleculaFormada.signalAll();
				System.out.println("Molecula de agua formada");
			} else { // molecula no formada
				moleculaFormada.await();
			}

			System.out.println("Atomo de Oxigeno "+id+" ha salido");
			atomosO = false;
			
			puedeEntrarO.signalAll();

		} finally{
			l.unlock();
		}
	}
}