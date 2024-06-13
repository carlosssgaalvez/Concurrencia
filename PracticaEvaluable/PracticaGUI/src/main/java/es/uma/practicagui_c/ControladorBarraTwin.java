package es.uma.practicagui_c;

import java.beans.PropertyChangeListener;

public class ControladorBarraTwin implements PropertyChangeListener {
    private Panel panel;

    public ControladorBarraTwin(Panel panel){
        this.panel = panel;
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("progress")){
            int progreso = (Integer) evt.getNewValue();
            panel.progreso1(progreso);
        }
    }
}
