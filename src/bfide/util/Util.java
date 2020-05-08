package bfide.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.function.Consumer;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Util {

    public static void alertError(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(exception.getMessage());

        // Create expandable Exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(new VBox(label, textArea));

        alert.show();
    }

    /**
     * Set the visible property of node to also control whether the node is
     * on the view.
     */
    public static void bindManagedToVisible(Node node) {
        node.managedProperty().bind(node.visibleProperty());
    }

    public static void currentScreenDo(Stage stage, Consumer<Screen> action) {
        ObservableList<Screen> screens = Screen.getScreensForRectangle(
            stage.getX(), stage.getY(),
            stage.getWidth(), stage.getHeight());
        if (screens.size() > 0)
            action.accept(screens.get(0));
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

    public static void flashTooltipAboveNode(
        Node node,
        Duration delay,
        String text
    ) {
        Tooltip notify = new Tooltip(text);

        Bounds bounds = node.localToScreen(node.getBoundsInLocal());
        notify.show(
            node,
            bounds.getMinX(),
            bounds.getMinY() + bounds.getHeight() + 2
        );

        new Transition() {

            {
                this.setDelay(delay);
                this.setCycleCount(1);
                this.setCycleDuration(Duration.millis(500));
                this.setOnFinished(e -> notify.hide());
            }

            @Override
            protected void interpolate(double frac) {
                notify.setOpacity(1 - frac);
            }

        }.play();
    }

    public static String readFile(File file) throws FileNotFoundException,
                                                    IOException {
        return readFile(new FileReader(file));
    }

    public static String readFile(Reader reader) throws FileNotFoundException,
                                                        IOException {
        BufferedReader buffReader = new BufferedReader(reader);
        if (buffReader == null)
            return null;

        StringBuilder document = new StringBuilder();

        buffReader.lines().forEach(
            line -> document.append(line).append('\n'));

        buffReader.close();

        return document.toString();
    }

}
