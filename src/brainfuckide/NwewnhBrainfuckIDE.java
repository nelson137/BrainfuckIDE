package brainfuckide;

import brainfuckide.util.BfLogger;
import static brainfuckide.util.Constants.CSS_STYLE;
import static brainfuckide.util.Constants.CSS_THEME_DARK;
import static brainfuckide.util.Constants.ICON;
import static brainfuckide.util.Constants.SPLASH_FXML;
import static brainfuckide.util.Constants.TITLE;
import static brainfuckide.util.Constants.WELCOME_FONT;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class NwewnhBrainfuckIDE extends Application {

    public void bf_init(final Stage stage, final String fxml)
            throws IOException {
        BfLogger.setActiveTags("interpreter");

        Font.loadFont(
            this.getClass().getResource(WELCOME_FONT).toExternalForm(),
            12);

        Parent root = FXMLLoader.load(this.getClass().getResource(fxml));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(CSS_STYLE, CSS_THEME_DARK);

        stage.setTitle(TITLE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(ICON));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.bf_init(stage, SPLASH_FXML);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
