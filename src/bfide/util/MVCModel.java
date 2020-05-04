package bfide.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public abstract class MVCModel {

    protected PropertyChangeSupport propertyChangeSupport;

    public MVCModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addAllPropertyChangeListeners(PropertyChangeListener... listeners) {
        for (PropertyChangeListener l : listeners)
            this.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, null, newValue);
    }

}
