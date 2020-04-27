package brainfuckide.ide.tabs.editor.visualizer;

import brainfuckide.ide.tabs.editor.interpreter.InterpreterModel;
import brainfuckide.util.ui.PulseAnimation;
import brainfuckide.util.Util;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Visualizer implements Initializable, PropertyChangeListener {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane root;

    @FXML
    private HBox cellViews;

    private CellView currentCell;
    public Animation cellPulseAnimation;
    private static final double TICK_RATE = 1_000;

    /**************************************************************************
     * Initialization
     *************************************************************************/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Prevent height from shrinking < height calculated from children:
        // https://stackoverflow.com/a/32997239/5673922
        this.scrollPane.minHeightProperty().bind(this.scrollPane.heightProperty());

        Util.bindManagedToVisible(this.scrollPane);

        this.setupVisualizer();

        this.cellPulseAnimation = new PulseAnimation(
            () -> this.currentCell,
            Duration.millis(TICK_RATE).multiply(.75)
        );
    }

    private void setupVisualizer() {
        for (int i=0; i<25; i++)
            this.addToTape();
        this.currentCell = (CellView) this.cellViews.getChildren().get(0);
        this.currentCell.setHighlighted(true);
    }

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    private void addToTape() {
        this.cellViews.getChildren().add(new CellView());
    }

    private void cleanupVisualizer() {
        this.cellViews.getChildren().clear();
    }

    public void resetVisualizer() {
        this.cleanupVisualizer();
        this.setupVisualizer();
    }

    public void setExecutionRate(Number rate) {
        this.cellPulseAnimation.setRate((double) rate);
    }

    public boolean isVisible() {
        return this.scrollPane.isVisible();
    }

    public void setVisible(boolean value) {
        this.scrollPane.setVisible(value);
    }

    private void setCurrentCell(int index) {
        if (index >= this.cellViews.getChildren().size())
            this.addToTape();

        this.currentCell.setHighlighted(false);
        this.currentCell = (CellView) this.cellViews.getChildren().get(index);
        this.currentCell.setHighlighted(true);
    }

    /**************************************************************************
     * MVC Communication
     *************************************************************************/

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (this.scrollPane.isVisible() == false)
            return;

        Object newValue = event.getNewValue();

        switch (event.getPropertyName()) {

            case InterpreterModel.CURSOR_RIGHT:
            case InterpreterModel.CURSOR_LEFT:
                this.setCurrentCell((int) newValue);
                break;

            case InterpreterModel.SET_VALUE:
                this.currentCell.setValue((Integer) newValue);
                break;

            case InterpreterModel.PRINT_CHAR:
                this.cellPulseAnimation.play();
                break;

            case InterpreterModel.READ_CHAR:
                break;

        }
    }

}
