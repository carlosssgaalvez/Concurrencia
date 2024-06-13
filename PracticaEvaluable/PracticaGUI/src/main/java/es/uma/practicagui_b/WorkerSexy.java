package es.uma.practicagui_b;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class WorkerSexy extends SwingWorker<Void, Primos> {

    private int n; //numero de primos a calcular
    private Panel panel;

    public WorkerSexy(int n,Panel panel){
        this.n = n;
        this.panel = panel;
        panel.limpiaAreaSexy();
    }

    // nos calcula el siguiente primo
    private long siguientePrimo(long p){
        long primo = p+1;
        boolean esPrimo = false;
        while (!esPrimo(primo)){
            primo++;
        }
        return primo;
    }

    private boolean esPrimo(long p){
        boolean primo = true;
        long i = 2;
        while ((i*2) <= p && primo){
            if (p % i == 0){
                primo = false;
            }
            i++;
        }
        return primo;
    }

    // calcula los primos twins
    @Override
    protected Void doInBackground() throws Exception {
        int numPrimos = 0;
        long primo1 = 2;

        while (numPrimos < n && !this.isCancelled()){
            long primo2 = primo1 + 6;
            if (esPrimo(primo2)){                                   // comprobamos que primo1 tiene un twin
                publish(new Primos(primo1,primo2,numPrimos));     // si lo tiene añadimos a la lista
                numPrimos++;
            }
            primo1 = siguientePrimo(primo1);
        }
        return null;
    }

    @Override
    protected void process(List<Primos> lista){
        panel.escribePrimosSexy(lista);
    }

    public void done(){
        try {
            get();
            panel.mensaje("Tarea completada");
        } catch (InterruptedException e) {
            System.out.println("Tarea cancelada");
            e.printStackTrace();
        } catch (ExecutionException | CancellationException e) {
            System.out.println("Tarea cancelada");
        }
    }
}
