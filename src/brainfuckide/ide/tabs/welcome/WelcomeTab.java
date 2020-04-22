package brainfuckide.ide.tabs.welcome;

import brainfuckide.ide.tabs.BfTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeTab extends BfTab {

    private static final String CSS_WELCOME = "welcome-content";

    private static final GlyphFont FONT_AWESOME =
        GlyphFontRegistry.font("FontAwesome");

    private final Button newFileButton;
    private final Button openFileButton;

    public WelcomeTab() {
        super("Welcome");

        Label text = new Label("Welcome");

        this.newFileButton = this.makeButton(
            "New File", FontAwesome.Glyph.FILE_ALT);
        this.openFileButton = this.makeButton(
            "Open File", FontAwesome.Glyph.FOLDER_OPEN_ALT);

        FlowPane buttonBox = new FlowPane(newFileButton, openFileButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setOrientation(Orientation.HORIZONTAL);
        buttonBox.setHgap(64);
        buttonBox.setVgap(64);

        HBox logoBox = new HBox(text, buttonBox);
        logoBox.getStyleClass().add(CSS_WELCOME);
        logoBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(logoBox, Priority.ALWAYS);

        this.root.getChildren().add(logoBox);
    }

    private Button makeButton(String text, FontAwesome.Glyph glyph) {
        Button button = new Button(text);
        button.setGraphic(
            FONT_AWESOME.create(glyph).size(48).color(Color.WHITE));
        button.setGraphicTextGap(24);
        return button;
    }

    @Override
    public Type getType() {
        return Type.WELCOME;
    }

    public void setOnNewFile(EventHandler<ActionEvent> handler) {
        this.newFileButton.setOnAction(handler);
    }

    public void setOnOpenFile(EventHandler<ActionEvent> handler) {
        this.openFileButton.setOnAction(handler);
    }

}
