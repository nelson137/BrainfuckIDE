package brainfuckide.ide.tabs.welcome;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeContent extends AnchorPane implements Initializable {

    private static final String FXML_PATH = "WelcomeContent.fxml";

    private static final GlyphFont FONT_AWESOME =
        GlyphFontRegistry.font("FontAwesome");

    private static final Color FA_GLYPH_COLOR =
        Color.web("#2e3843").deriveColor(0, 1, 1.55, 1);

    @FXML
    private Button newFileButton;

    @FXML
    private Button openFileButton;

    public WelcomeContent() {
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
    }

    private Glyph makeGlyph(FontAwesome.Glyph glyph) {
        return FONT_AWESOME.create(glyph).size(48).color(FA_GLYPH_COLOR);
    }

    public void setOnNewFile(EventHandler<ActionEvent> handler) {
        this.newFileButton.setOnAction(handler);
    }

    public void setOnOpenFile(EventHandler<ActionEvent> handler) {
        this.openFileButton.setOnAction(handler);
    }

}
