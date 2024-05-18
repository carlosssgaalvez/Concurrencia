package Fumadores;

import java.util.Random;

public class Agente extends Thread {
	private Mesa mesa;
	private Random ingrediente;
	public Agente(Mesa mesa) {
		this.mesa = mesa;
		ingrediente = new Random();
	}
	public void run() {
		try {
			while(!this.isInterrupted()) {
				mesa.poneIngrediente(ingrediente.nextInt(3)); //el random sera 1 2 o 3
			}
		}catch(InterruptedException e) {}
	}
}
