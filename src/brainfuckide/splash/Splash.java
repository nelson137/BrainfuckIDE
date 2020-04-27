package brainfuckide.splash;

import static brainfuckide.util.Constants.CSS_STYLE;
import static brainfuckide.util.Constants.CSS_THEME_DARK;
import static brainfuckide.util.Constants.MAIN_FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        return new PauseTransition(Duration.millis(1000));
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
