package es.uma.practicagui_c;

import java.beans.PropertyChangeListener;

public class ControladorBarraCousin implements PropertyChangeListener {
    private Panel panel;

    public ControladorBarraCousin(Panel panel){
        this.panel = panel;
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("progress")){
            int progreso = (Integer) evt.getNewValue();
            panel.progreso2(progreso);
        }
    }
}
