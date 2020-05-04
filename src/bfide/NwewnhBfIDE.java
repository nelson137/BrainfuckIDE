package bfide;

import bfide.util.BfLogger;
import static bfide.util.Constants.CSS_STYLE;
import static bfide.util.Constants.CSS_THEME_DARK;
import static bfide.util.Constants.ICON;
import static bfide.util.Constants.SPLASH_FXML;
import static bfide.util.Constants.TITLE;
import static bfide.util.Constants.WELCOME_FONT;
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
public class NwewnhBfIDE extends Application {

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
