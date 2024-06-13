package es.uma.practicagui_a;

public class Primos {
    private long primo1;
    private long primo2;
    private int posicion;

    public Primos(long primo1, long primo2, int posicion) {
        this.primo1 = primo1;
        this.primo2 = primo2;
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return (posicion+":("+primo1+","+primo2+")");
    }
}
