package brainfuckide.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Util {

    public static final GlyphFont FONT_AWESOME =
        GlyphFontRegistry.font("FontAwesome");

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

    public static Slider cloneSlider(Slider reference) {
        Slider clone = new Slider();
        clone.setMin(reference.getMin());
        clone.setMax(reference.getMax());
        clone.setValue(reference.getValue());
        clone.setBlockIncrement(reference.getBlockIncrement());
        clone.setShowTickMarks(reference.isShowTickMarks());
        clone.setShowTickLabels(reference.isShowTickLabels());
        clone.setMajorTickUnit(reference.getMajorTickUnit());
        clone.setMinorTickCount(reference.getMinorTickCount());
        clone.setSnapToTicks(reference.isSnapToTicks());
        return clone;
    }

}
