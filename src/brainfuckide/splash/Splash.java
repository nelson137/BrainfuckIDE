package brainfuckide.splash;

import static brainfuckide.util.Constants.CSS_STYLE;
import static brainfuckide.util.Constants.CSS_THEME_DARK;
import static brainfuckide.util.Constants.MAIN_FXML;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class Splash implements Initializable {

    public static final String CSS_SPLASH_FADE = "splash-fade-pane";

    @FXML
    private AnchorPane content;

    @FXML
    private Label loadingBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animation animation = new SequentialTransition(
            this.makeLoadingAnimation(),
            this.makeFadeOutAnimation()
        );
        animation.setOnFinished(event -> this.loadMainScene());
        animation.play();
    }

    private Stage getStage() {
        return (Stage) this.content.getScene().getWindow();
    }

    private Animation makeLoadingAnimation() {
        return new Transition() {

            {
                setDelay(Duration.millis(250));
                setCycleCount(1);
                setCycleDuration(Duration.millis(4000));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            private String repeatChar(char c, int count) {
                char s[] = new char[count];
                Arrays.fill(s, c);
                return new String(s);
            }

            @Override
            protected void interpolate(double frac) {
                int width = loadingBar.getText().length() - 2;
                String filled = this.repeatChar('>', (int)(width * frac));
                String empty = this.repeatChar(' ', width - filled.length());
                loadingBar.setText(String.format("[" + filled + empty + "]"));
            }

        };
    }

    private Animation makeFadeOutAnimation() {
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.millis(1000));
        fadeOut.setNode(this.content);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        return fadeOut;
    }

    private void loadMainScene() {
        try {
            Parent mainRoot = FXMLLoader.load(
                this.getClass().getResource(MAIN_FXML));

            Scene mainScene = new Scene(mainRoot);
            mainScene.getStylesheets().addAll(CSS_STYLE, CSS_THEME_DARK);

            this.getStage().setScene(mainScene);
        } catch (IOException ex) {
            Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
