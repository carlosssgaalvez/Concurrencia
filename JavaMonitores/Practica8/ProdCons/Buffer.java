package ProdCons;
public class Buffer {
	private int p; /*posicion por la que se va produciendo*/
	private int c[]; /*posicion por la que se va consumiendo cada consumidor*/
	private int[] elem; 
	//TODO


	//para cada consumidor, numero de elementos que tiene por consumir
	private int[] porConsumir;

	//numero de lecturas que se han hecho de cada elemento
	private int[] numLecturas;

	// numero huecos libres para poder consumir
	private int huecos;

	// numero de consumidores
	private int numC;
	
	public Buffer(int N, int nCon)
	{
		//TODO
		numC = nCon;
		huecos = N;
		p = 0;
		c = new int[numC];
		porConsumir = new int[numC];
		for(int i = 0; i < numC; i++){
			c[i] = 0;
			porConsumir[i] = 0;
		}
		elem = new int[N];
		numLecturas = new int[N];
		for(int i = 0; i < N; i++){
			numLecturas[i] = 0;
		}	

	}

	
	//Productor
	public synchronized void producir(int id, int e) throws InterruptedException{		
		//TODO
		while(huecos == 0){
			wait();
		}		

		elem[p] = e;

		System.out.println("Productor "+id+" produce "+e+" en la posicion "+p);
		System.out.print("Elem: ");
		mostrarDatos(elem);

		for(int i = 0; i < numC; i++){
			porConsumir[i]++;
		}

		System.out.print("Por consumir: ");
		mostrarDatos(porConsumir);

		huecos--;
		System.out.println("Huecos: "+huecos);

		numLecturas[p] = 0;
		System.out.print("Num lecturas: ");
		mostrarDatos(numLecturas);

		p = (p+1)%elem.length;
		notifyAll();
		
	}
	
	public int consumir(int id) throws InterruptedException{
		while(porConsumir[id] == 0) {
			wait();
		}	

		int pos = c[id];
		int item = elem[pos];
		System.out.println("Consumidor "+id+" consume "+item+" en la posicion "+pos);
		System.out.print("Elem: ");
		mostrarDatos(elem);

		porConsumir[id]--;

		System.out.print("Por consumir: ");
		mostrarDatos(porConsumir);

		numLecturas[pos]++;
		System.out.print("Num lecturas: ");
		mostrarDatos(numLecturas);

		if(numLecturas[pos] == numC){
			huecos++;
			System.out.println("Huecos: "+huecos);
		}

		c[id] = (c[id]+1)%elem.length;
		notifyAll();
		
		return item;
	}

	public void mostrarDatos(int[] datos) {
		for(int i = 0; i < datos.length; i++)
			System.out.print(datos[i]+" ");
		System.out.println();
	}
	
	public static void main(String[] args) {
		final int NP = 2;
		final int NC = 3;
		Buffer b = new Buffer(10, NC);
		Productor productores[] = new Productor[NP]; 
		Consumidor consumidores[] = new Consumidor[NC];
				
		for(int i=0; i < NP; i++)
			productores[i]= new Productor(i, b,20);
		for(int i =0; i < NC; i++)
			consumidores[i] = new Consumidor(i, b,40);
		for(int i=0; i < NP; i++)
			productores[i].start();
		for(int i=0; i < NC; i++)
			consumidores[i].start();
	}
}
