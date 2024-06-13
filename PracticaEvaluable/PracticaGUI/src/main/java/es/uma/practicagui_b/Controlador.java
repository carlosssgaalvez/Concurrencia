package es.uma.practicagui_b;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener {
    private Panel panel;
    private WorkerTwin worker1;
    private WorkerCousin worker2;
    private WorkerSexy worker3;

    //Constructor
    public Controlador(Panel panel) {
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("numero1")) { // primos twin
            worker1 = new WorkerTwin(panel.numero1(), panel);
            worker1.execute();
        } else if (e.getActionCommand().equals("numero2")) { // primos cousin
            worker2 = new WorkerCousin(panel.numero2(), panel);
            worker2.execute();
        } else if (e.getActionCommand().equals("numero3")) { // primos sexy
            worker3 = new WorkerSexy(panel.numero3(), panel);
            worker3.execute();
        } else if (e.getActionCommand().equals("fin")) { // cancelar
            if (worker1!=null){
                worker1.cancel(true);
                panel.mensaje("Tarea cancelada");
            } else if (worker2!=null){
                worker2.cancel(true);
                panel.mensaje("Tarea cancelada");
            } else if (worker3!=null){
                worker3.cancel(true);
                panel.mensaje("Tarea cancelada");
            }
        }
    }

}
