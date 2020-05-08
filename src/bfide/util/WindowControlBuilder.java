package bfide.util;

import java.util.LinkedList;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class WindowControlBuilder {

    protected static class DragOffset {
        public double x, y;
    }

    protected static final LinkedList<DragOffset> OFFSETS = new LinkedList<>();

    protected DragOffset dragOffset;

    protected Window window;
    protected Node node;

    protected Cursor cursorOnEnter, cursorOnPress, cursorOnExit;
    protected Cursor implCursorOnRelease;

    protected Runnable onSecondaryClick = () -> {};

    public WindowControlBuilder(Window window) {
        this();
        this.window = window;
    }

    protected WindowControlBuilder() {
        this.dragOffset = new DragOffset();
        OFFSETS.add(dragOffset);

        this.cursorOnEnter = Cursor.DEFAULT;
        this.cursorOnPress = Cursor.DEFAULT;
        this.cursorOnExit = Cursor.DEFAULT;
    }

    public Window getWindow() {
        return this.window;
    }

    static protected Stage getEventStage(Event event) {
        return (Stage) ((Node)event.getSource()).getScene().getWindow();
    }

    /**************************************************************************
     * Manipulators
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public WindowControlBuilder node(Node node) {
        this.node = node;
        return this;
    }

    public WindowControlBuilder cursorOnEnter(Cursor cursor) {
        this.cursorOnEnter = cursor;
        return this;
    }

    public WindowControlBuilder cursorOnPress(Cursor cursor) {
        this.cursorOnPress = cursor;
        return this;
    }

    public WindowControlBuilder cursorOnExit(Cursor cursor) {
        this.cursorOnExit = cursor;
        return this;
    }

    public WindowControlBuilder onSecondaryClick(Runnable runnable) {
        this.onSecondaryClick = runnable;
        return this;
    }

    public void build() {
        if (this.node == null)
            return;

        this.node.addEventHandler(
            MouseEvent.MOUSE_ENTERED, event -> this.onMouseEnter(event));

        this.node.addEventHandler(
            MouseEvent.MOUSE_CLICKED, event -> this.onMouseClick(event));

        this.node.addEventHandler(
            MouseEvent.MOUSE_PRESSED, event -> this.onMousePress(event));

        this.node.addEventHandler(
            MouseEvent.MOUSE_DRAGGED, event -> this.onMouseDrag(event));

        this.node.addEventHandler(
            MouseEvent.MOUSE_RELEASED, event -> this.onMouseRelease(event));

        this.node.addEventHandler(
            MouseEvent.MOUSE_EXITED, event -> this.onMouseExit(event));
    }

    // </editor-fold>

    /**************************************************************************
     * Event Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    protected void onMouseEnter(MouseEvent event) {
        if (event.isPrimaryButtonDown() == false)
            this.node.setCursor(this.cursorOnEnter);
    }

    protected void onMouseClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.SECONDARY))
            this.onSecondaryClick.run();
    }

    protected void onMousePress(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            this.implCursorOnRelease = this.node.getCursor();
            this.node.setCursor(this.cursorOnPress);
            this.dragOffset.x = event.getSceneX();
            this.dragOffset.y = event.getSceneY();
        }
    }

    protected void onMouseDrag(MouseEvent event) {
        boolean isCursorPress = this.node.getCursor() == this.cursorOnPress;
        if (event.isPrimaryButtonDown() && isCursorPress) {
            this.getWindow().setX(event.getScreenX() - this.dragOffset.x);
            this.getWindow().setY(event.getScreenY() - this.dragOffset.y);
        }
    }

    protected void onMouseRelease(MouseEvent event) {
        if (event.isPrimaryButtonDown() == false)
            this.node.setCursor(this.implCursorOnRelease);
    }

    protected void onMouseExit(MouseEvent event) {
        if (event.isPrimaryButtonDown() == false)
            this.node.setCursor(this.cursorOnExit);
    }

    // </editor-fold>

}
