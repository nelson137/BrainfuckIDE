package brainfuckide;

import brainfuckide.util.BfLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class BrainfuckIDE extends Application {

    private static final String TITLE = "Brainfuck IDE";

    private static final String START_PAGE = "/brainfuckide/ide/IDE.fxml";

    private static final String CSS_STYLE = "/resources/css/style.css";
    private static final String CSS_DARK = "/resources/css/dark-theme.css";
    private static final String CSS_LIGHT = "/resources/css/light-theme.css";

    private static final String WELCOME_FONT =
        "/resources/fonts/MuseoSans_500.otf";

    @Override
    public void start(Stage stage) throws Exception {
        BfLogger.setActiveTags("interpreter");

        Font.loadFont(
            this.getClass().getResource(WELCOME_FONT).toExternalForm(),
            12);

        Parent root = FXMLLoader.load(this.getClass().getResource(START_PAGE));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(CSS_STYLE, CSS_DARK);

        stage.setTitle(TITLE);
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
