package brainfuckide.util;

import javafx.scene.Node;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Util {

    /**
     * Set the visible property of node to also control whether the node is
     * on the view.
     */
    public static void bindManagedToVisible(Node node) {
        node.managedProperty().bind(node.visibleProperty());
    }

}
