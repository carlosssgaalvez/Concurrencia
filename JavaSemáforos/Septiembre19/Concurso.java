import java.util.concurrent.Semaphore;

public class Concurso {
	private boolean concursoTerminado = false;
	private volatile int[] nLanzamientos = new int[] {0,0};
	private volatile int[] nCaras = new int[] {0,0};
	private volatile int[] juegosGanados = new int[] {0,0};

	private Semaphore puedeTirarJugador1 = new Semaphore(1); // id = 0
	private Semaphore puedeTirarJugador2 = new Semaphore(1); // id = 1
	private Semaphore mutex = new Semaphore(1);

	

	
	public void tirarMoneda(int id,boolean cara) throws InterruptedException {
		if(id == 0){ // jugador 0;
			puedeTirarJugador1.acquire();
			mutex.acquire();
			nLanzamientos[0]++;
			if(cara == true){
				nCaras[0]++;
			}
			if(nLanzamientos[0] < 10){
				puedeTirarJugador1.release();
			} else if(nLanzamientos[0] + nLanzamientos[1] == 20){ // si solo ha terminado este se sale del metodo
				nLanzamientos[0] = 0;
				nLanzamientos[1] = 0;
				if(nCaras[0]> nCaras[1]){
					juegosGanados[0]++;
					System.out.println("Ha ganado la partida el jugador 0 con "+nCaras[0]+" caras");
				} else if(nCaras[1] > nCaras[0]){
					juegosGanados[1]++;
					System.out.println("Ha ganado la partida el jugador 1 con "+nCaras[1]+" caras");
				} else {
					System.out.println("El juega ha empatado");
				}
				nCaras[0] = 0;
				nCaras[1] = 0;
				if(juegosGanados[0] == 3 || juegosGanados[1] == 3){ //uno de los dos ha ganado
					concursoTerminado = true;
					System.out.println("Final del concurso. Ha ganado el jugador "+ (juegosGanados[0]>juegosGanados[1]? 0 : 1));
				} else {
					puedeTirarJugador1.release();
					puedeTirarJugador2.release();
				}
			}
			mutex.release();
		} else { // jugador 1;
			puedeTirarJugador2.acquire();
			mutex.acquire();
			nLanzamientos[1]++;
			if(cara == true){
				nCaras[1]++;
			}

			if(nLanzamientos[1] < 10){
				puedeTirarJugador2.release();
			} else if(nLanzamientos[0] + nLanzamientos[1] == 20){ // si solo ha terminado este se sale del metodo
				nLanzamientos[0] = 0;
				nLanzamientos[1] = 0;
				if(nCaras[0]> nCaras[1]){
					juegosGanados[0]++;
					System.out.println("Ha ganado la partida el jugador 0 con "+nCaras[0]+" caras");
				} else if(nCaras[1] > nCaras[0]){
					juegosGanados[1]++;
					System.out.println("Ha ganado la partida el jugador 1 con "+nCaras[1]+" caras");
				} else {
					System.out.println("El juega ha empatado");
				}
				nCaras[0] = 0;
				nCaras[1] = 0;
				if(juegosGanados[0] == 3 || juegosGanados[1] == 3){ //uno de los dos ha ganado
					concursoTerminado = true;
					System.out.println("Final del concurso. Ha ganado el jugador "+ (juegosGanados[0]>juegosGanados[1]? 0 : 1));
				} else {
					puedeTirarJugador1.release();
					puedeTirarJugador2.release();
				}
			}
			mutex.release();
		}
		
		
	}	
	
	public boolean concursoTerminado() {	
		return concursoTerminado; 
	}
}