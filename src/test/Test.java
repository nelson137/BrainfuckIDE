package test;

import brainfuckide.BrainfuckIDE;
import static brainfuckide.util.Constants.MAIN_FXML;
import java.io.IOException;
import javafx.stage.Stage;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Test extends BrainfuckIDE {

    @Override
    public void start(Stage stage) throws IOException {
        super.bf_init(stage, MAIN_FXML);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
