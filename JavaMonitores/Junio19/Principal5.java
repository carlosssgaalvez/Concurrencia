
public class Principal5 {

	public static void main(String[] args)
	{
		Tiovivo t = new Tiovivo(5);
		Operario o = new Operario(t);
		Pasajero4[] personas = new Pasajero4[7];
		for(int i =0; i<7; i++)
		{
			personas[i] = new Pasajero4(i,t);
		}
		
		
		for(int i =0; i<7; i++)
		{
			personas[i].start();
		}
		o.start();
		
		
	}
}
