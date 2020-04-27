package brainfuckide;

import brainfuckide.util.BfLogger;
import static brainfuckide.util.Constants.CSS_STYLE;
import static brainfuckide.util.Constants.CSS_THEME_DARK;
import static brainfuckide.util.Constants.SPLASH_FXML;
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
public class BrainfuckIDE extends Application {

    private static final String TITLE = "Brainfuck IDE";

    private static final String WELCOME_FONT =
        "/resources/fonts/MuseoSans_500.otf";

    private static final String ICON = "/resources/images/icon.png";

    @Override
    public void start(Stage stage) throws Exception {
        BfLogger.setActiveTags("interpreter");

        Font.loadFont(
            this.getClass().getResource(WELCOME_FONT).toExternalForm(),
            12);

        Parent root = FXMLLoader.load(this.getClass().getResource(SPLASH_FXML));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(CSS_STYLE, CSS_THEME_DARK);

        stage.setTitle(TITLE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(ICON));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
