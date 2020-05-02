package brainfuckide.ide.popups;

import brainfuckide.util.WindowControlBuilder;
import javafx.scene.Cursor;
import javafx.stage.Popup;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class AsciiPopup extends Popup {

    private final AsciiGridPane grid;

    private static final String CSS_ASCII_TABLE = "ascii-table";

    public AsciiPopup() {
        super();

        this.grid = new AsciiGridPane();

        super.getContent().add(this.grid);
        this.grid.getStyleClass().add(CSS_ASCII_TABLE);

        new WindowControlBuilder(this.getScene().getWindow())
            .node(this.grid)
            .cursorOnEnter(Cursor.OPEN_HAND)
            .cursorOnPress(Cursor.MOVE)
            .onSecondaryClick(() -> super.hide())
            .build();
    }

}
