package brainfuckide.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

    public interface ScreenAction {
        void doAction(Screen screen);
    }

    public static void currentScreenDo(Stage stage, ScreenAction action) {
        ObservableList<Screen> screens = Screen.getScreensForRectangle(
            stage.getX(), stage.getY(),
            stage.getWidth(), stage.getHeight());
        if (screens.size() > 0)
            action.doAction(screens.get(0));
    }

}
