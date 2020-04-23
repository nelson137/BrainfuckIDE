package brainfuckide.ide.popups;

import brainfuckide.util.DragOffset;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Window;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class AsciiPopup extends Popup {

    private final AsciiGridPane grid;

    private final DragOffset dragOffset;

    private static final String CSS_ASCII_TABLE = "ascii-table";

    public AsciiPopup() {
        super();

        this.grid = new AsciiGridPane();

        this.dragOffset = new DragOffset();

        this.setupUI();

        this.setupListeners();
    }

    private void setupUI() {
        super.getContent().add(this.grid);
        this.grid.getStyleClass().add(CSS_ASCII_TABLE);
    }

    private void setupListeners() {
        this.grid.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (event.isPrimaryButtonDown() == false)
                this.grid.setCursor(Cursor.OPEN_HAND);
        });

        this.grid.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isSecondaryButtonDown()) {
                super.hide();
                return;
            }

            if (event.isPrimaryButtonDown() == false)
                return;

            this.dragOffset.x = event.getSceneX();
            this.dragOffset.y = event.getSceneY();
            this.grid.setCursor(Cursor.MOVE);
        });

        this.grid.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isPrimaryButtonDown() == false)
                return;
            Window window = super.getScene().getWindow();
            window.setX(event.getScreenX() - this.dragOffset.x);
            window.setY(event.getScreenY() - this.dragOffset.y);
        });

        this.grid.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (event.isPrimaryButtonDown())
                return;
            this.grid.setCursor(Cursor.OPEN_HAND);
        });

        this.grid.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (event.isPrimaryButtonDown() == false)
                this.grid.setCursor(Cursor.DEFAULT);
        });
    }

}
