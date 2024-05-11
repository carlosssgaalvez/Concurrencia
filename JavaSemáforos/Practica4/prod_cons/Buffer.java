package prod_cons;

public class Buffer {
    private int tam;
    private int[] elementos;
    private volatile int nelem = 0; // volatil porque la van a tocar varias hebras a la vez;
    private volatile int c = 0;     //consumirdor
    private volatile int p = 0;     //productor
    private volatile Peterson peterson;

    public Buffer(int n){
        if(n>0){
            this.tam = n;
            elementos = new int[tam];
            peterson = new Peterson();
        }
    }

    public void producir(int item){
        while(nelem == tam){
            Thread.yield();         //bloquea 
        }
        peterson.preprotocolo0();
        System.out.println("El productor ha producido el item "+ item);
        elementos[p] = item;
        p = (p+1)%tam;
        nelem++;
        peterson.postprotocolo0();
    }

    public int consumir(){
        while(nelem == 0){
            Thread.yield();         // bloqueamos hasta que podamos consumir
        }
        peterson.preprotocolo1();
        int item = elementos[c];
        System.out.println("El productor ha consumido el item "+ item);
        elementos[c] = 0;
        c = (c+1)%tam;
        nelem--;
        peterson.postprotocolo1();
        return item;
    }
    
}
