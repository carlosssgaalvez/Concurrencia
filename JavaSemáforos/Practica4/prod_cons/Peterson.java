package prod_cons;

public class Peterson {
    private volatile int turno;
    private volatile boolean f0;
    private volatile boolean f1;

    public Peterson() {
        turno = 0;
        f0 = false;
        f1 = false;
    }

    public void preprotocolo0() {
        f0 = true;
        turno = 1;
        while (f1 && turno == 1) {
            Thread.yield();
        }
    }

    public void preprotocolo1() {
        f1 = true;
        turno = 0;
        while (f0 && turno == 0) {
            Thread.yield();
        }
    }

    public void postprotocolo0() {
        f0 = false;
    }

    public void postprotocolo1() {
        f1 = false;
    }
}
