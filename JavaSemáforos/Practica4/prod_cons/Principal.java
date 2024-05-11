package prod_cons;

public class Principal {
	public static void main(String[] args) {
		int s = 6, n = 30;
		Buffer b = new Buffer(s);
		Productor p = new Productor(b, n);
		Consumidor c = new Consumidor(b,n);
		p.start();
		c.start();
	}
}
