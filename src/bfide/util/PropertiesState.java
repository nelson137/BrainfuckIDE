package bfide.util;

import java.util.HashMap;
import javafx.beans.property.Property;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class PropertiesState {

    private final HashMap<Property,Object> state = new HashMap<>();

    public PropertiesState(Property... properties) {
        for (Property p : properties)
            this.state.put(p, null);
    }

    public void save() {
        this.state.replaceAll((Property k, Object v) -> k.getValue());
    }

    public void restore() {
        this.state.forEach((Property k, Object v) -> k.setValue(v));
    }

}
