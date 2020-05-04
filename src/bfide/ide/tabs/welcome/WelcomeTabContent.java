package bfide.ide.tabs.welcome;

import static bfide.util.Constants.FONT_AWESOME;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeTabContent extends AnchorPane implements Initializable {

    private static final String FXML_PATH = "WelcomeTabContent.fxml";

    @FXML
    private Button newFileButton;

    @FXML
    private Button openFileButton;

    @FXML
    private Button howToButton;

    public WelcomeTabContent() {
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
        this.newFileButton.setGraphic(this.makeGlyph(
            FontAwesome.Glyph.FILE_ALT));
        this.openFileButton.setGraphic(this.makeGlyph(
            FontAwesome.Glyph.FOLDER_OPEN_ALT));
        this.howToButton.setGraphic(this.makeGlyph(
            FontAwesome.Glyph.QUESTION_CIRCLE));
    }

    private Glyph makeGlyph(FontAwesome.Glyph glyph) {
        return FONT_AWESOME.create(glyph).size(48);
    }

    public void setOnNewFile(EventHandler<ActionEvent> handler) {
        this.newFileButton.setOnAction(handler);
    }

    public void setOnOpenFile(EventHandler<ActionEvent> handler) {
        this.openFileButton.setOnAction(handler);
    }

    public void setOnHowTo(EventHandler<MouseEvent> handler) {
        this.howToButton.setOnMouseClicked(handler);
    }

}
