public class Principal4 {

	public static void main(String[] args) {
		Barca b = new Barca();
		Capitan p = new Capitan(b);
		Pasajero2[] pas = new Pasajero2[18];
		for (int i=0; i<pas.length; i++){
			pas[i] = new Pasajero2(i,i%2==0?0:1,b);
		}
		p.start();
		for (int i=0; i<pas.length; i++){
			pas[i].start();
		}
	}

}
