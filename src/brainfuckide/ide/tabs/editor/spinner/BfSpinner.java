package brainfuckide.ide.tabs.editor.spinner;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class BfSpinner implements Initializable {

    @FXML
    private Label label;

    private Animation timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.label.toFront();

        this.timeline = new SequentialTransition(
            new PauseTransition(Duration.millis(1_000)),
            // Wind-up
            new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(
                this.label.rotateProperty(), -20, Interpolator.EASE_IN
            ))),
            // Spin
            new Timeline(new KeyFrame(Duration.millis(650), new KeyValue(
                this.label.rotateProperty(), 375, Interpolator.EASE_BOTH
            ))),
            // Cool-down
            new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(
                this.label.rotateProperty(), 360, Interpolator.EASE_OUT
            )))
        );
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        this.label.setRotate(0);
        this.label.setVisible(true);
        this.timeline.play();
    }

    public void play() {
        this.timeline.play();
        this.label.setVisible(true);
    }

    public void pause() {
        this.label.setVisible(false);
        this.timeline.pause();
    }

    public void stop() {
        this.label.setVisible(false);
        this.timeline.stop();
    }

}
