import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Concurso {
	private int[] partidasGanadas = new int [2];
	private int[] nCaras = new int[2];
	private int tiradasJugador0 = 0;
	private int tiradasJugador1 = 0;
	private int nJuegos = 1;
	private boolean resultadoImpreso = false;

	private Lock l = new ReentrantLock();
	private Condition puedeTirarJugador0 = l.newCondition();
	private Condition puedeTirarJugador1 = l.newCondition();

	public Concurso(){
		for(int i = 0; i < partidasGanadas.length; i++){
			partidasGanadas[i] = 0;
			nCaras[i] = 0;
		}
	}

	
	public void tirarMoneda(int id,boolean cara) throws InterruptedException {
		l.lock();
		try{
			if(id == 0){
				while(tiradasJugador0 == 10){
					puedeTirarJugador0.await();
				}
				tiradasJugador0++;
				if(cara){
					nCaras[0]++;
				}
				if(tiradasJugador0 == 10 && tiradasJugador1 == 10){ // ronda terminada
					if(nCaras[0]>nCaras[1]){
						partidasGanadas[0]++;
						System.out.println("Juego "+nJuegos+": Ha ganado la partida el jugador 0 con "+nCaras[0]+" caras");
					} else if(nCaras[0]<nCaras[1]){
						partidasGanadas[1]++;
						System.out.println("Juego "+nJuegos+": Ha ganado la partida el jugador 1 con "+nCaras[1]+" caras");
					} else {
						System.out.println("El juego ha empatado");
					}
					nJuegos++;
					nCaras[0] = 0;
					nCaras[1] = 0;
					tiradasJugador0 = 0;
					tiradasJugador1 = 0;
					puedeTirarJugador1.signal();
				}

			} else { // jugador 1
				while(tiradasJugador1 == 10){
					puedeTirarJugador1.await();
				}
				tiradasJugador1++;
				if(cara){
					nCaras[1]++;
				}
				if(tiradasJugador0 == 10 && tiradasJugador1 == 10){ // ronda terminada
					if(nCaras[0]>nCaras[1]){
						partidasGanadas[0]++;
						System.out.println("Juego "+nJuegos+": Ha ganado la partida el jugador 0 con "+nCaras[0]+" caras");
					} else if(nCaras[0]<nCaras[1]){
						partidasGanadas[1]++;
						System.out.println("Juego "+nJuegos+": Ha ganado la partida el jugador 1 con "+nCaras[1]+" caras");
					} else {
						System.out.println("El juego ha empatado");
					}
					nJuegos++;
					nCaras[0] = 0;
					nCaras[1] = 0;
					tiradasJugador0 = 0;
					tiradasJugador1 = 0;
					puedeTirarJugador0.signal();
				}
			}

		}finally{
			l.unlock();
		}
		
	}	
	
	public boolean concursoTerminado() {
		l.lock();
		try{
			if((partidasGanadas[0] == 3 || partidasGanadas[1] == 3) && !resultadoImpreso){
				resultadoImpreso = true;
				System.out.println("Final del concurso. Ha ganado el jugador "+((partidasGanadas[0]==3)? 0 : 1));
				return true;
			} else {
				return false;
			}
		}finally{
			l.unlock();
		}
	}
}