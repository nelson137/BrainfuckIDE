package brainfuckide.ide.tabs.howto;

import brainfuckide.ide.IDEController;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class HowToTabContent extends VBox implements Initializable {

    private static final String FXML_PATH = "HowToTabContent.fxml";
    private static final String EXAMPLES_DIR = "/resources/examples";

    private final IDEController controller;

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;

    private HashMap<Button,String> buttonFiles;

    public HowToTabContent(IDEController controller) {
        super();

        this.controller = controller;

        this.buttonFiles = new HashMap<>();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource(FXML_PATH));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.buttonFiles.put(button1, "add-to-tape.bf");
    }

    @FXML
    public void openExampleFile(ActionEvent event) {
        String fileName = this.buttonFiles.get((Button) event.getSource());
        this.controller.newEditorTab().openResource(
            Paths.get(EXAMPLES_DIR, fileName).toFile());
    }

}
