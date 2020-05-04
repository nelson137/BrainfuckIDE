package test;

import bfide.NwewnhBfIDE;
import static bfide.util.Constants.MAIN_FXML;
import java.io.IOException;
import javafx.stage.Stage;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class Test extends NwewnhBfIDE {

    @Override
    public void start(Stage stage) throws IOException {
        super.bf_init(stage, MAIN_FXML);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
