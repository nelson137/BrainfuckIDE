package brainfuckide.ide.popups;

import brainfuckide.ide.popups.AsciiGridPane;
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

    private static class DragDelta {
        double x, y;
    }
    private final DragDelta dragDelta;

    private static final String CSS_ASCII_TABLE = "ascii-table";

    public AsciiPopup() {
        super();

        this.grid = new AsciiGridPane();

        this.dragDelta = new DragDelta();

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
            Window window = super.getScene().getWindow();
            this.dragDelta.x = window.getX() - event.getScreenX();
            this.dragDelta.y = window.getY() - event.getScreenY();
            this.grid.setCursor(Cursor.MOVE);
        });

        this.grid.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isPrimaryButtonDown() == false)
                return;
            Window window = super.getScene().getWindow();
            window.setX(event.getScreenX() + this.dragDelta.x);
            window.setY(event.getScreenY() + this.dragDelta.y);
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
