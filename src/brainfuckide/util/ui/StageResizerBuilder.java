package brainfuckide.util.ui;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class StageResizerBuilder {

    protected class ResizeOffset {
        public double x, y, w, h;
    }

    private Stage stage;
    private Scene scene;

    private Cursor cursorEdge;

    private static final double T = 8;
    private static final double MIN_WIDTH = 800;
    private static final double MIN_HEIGHT = 600;

    public StageResizerBuilder stage(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
        return this;
    }

    public void build() {
        this.scene.addEventFilter(
            MouseEvent.MOUSE_MOVED, event -> this.onMouseMove(event));

        this.scene.addEventFilter(
            MouseEvent.MOUSE_DRAGGED, event -> this.onMouseDrag(event));

        for (Node child : this.scene.getRoot().getChildrenUnmodifiable())
            this.addListenerDeeply(child);
    }

    private void addListenerDeeply(Node node) {
        node.addEventFilter(
            MouseEvent.MOUSE_MOVED, event -> this.onMouseMove(event));

        node.addEventFilter(
            MouseEvent.MOUSE_DRAGGED, event -> this.onMouseDrag(event));

        if (node instanceof Parent)
            for (Node child : ((Parent) node).getChildrenUnmodifiable())
                this.addListenerDeeply(child);
    }

    private static boolean diffThreshold(double a, double b, double threshold) {
        return Math.abs(a - b) < threshold;
    }

    private boolean setWidth(double width) {
        this.stage.setWidth(Math.max(width, MIN_WIDTH));
        return width > MIN_WIDTH;
    }

    private boolean setHeight(double height) {
        this.stage.setHeight(Math.max(height, MIN_HEIGHT));
        return height > MIN_HEIGHT;
    }

    protected void onMouseMove(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        double w = this.stage.getWidth();
        double h = this.stage.getHeight();

        if (diffThreshold(x, w, T) && diffThreshold(y, h, T))
            this.cursorEdge = Cursor.SE_RESIZE;
        else if (diffThreshold(x, 0, T) && diffThreshold(y, h, T))
            this.cursorEdge = Cursor.SW_RESIZE;
        else if (diffThreshold(x, w, T))
            this.cursorEdge = Cursor.E_RESIZE;
        else if (diffThreshold(y, h, T))
            this.cursorEdge = Cursor.S_RESIZE;
        else if (diffThreshold(x, 0, T))
            this.cursorEdge = Cursor.W_RESIZE;
        else
            this.cursorEdge = Cursor.DEFAULT;

        this.scene.setCursor(this.cursorEdge);
    }

    protected void onMouseDrag(MouseEvent event) {
        if (this.cursorEdge.equals(Cursor.DEFAULT))
            return;

        double stageX = this.stage.getX();
        double stageY = this.stage.getY();
        double stageW = this.stage.getWidth();

        double eventX = event.getScreenX();
        double eventY = event.getScreenY();

        if (this.cursorEdge.equals(Cursor.SE_RESIZE)) {
            this.setWidth(eventX - stageX);
            this.setHeight(eventY - stageY);
        } else if (this.cursorEdge.equals(Cursor.SW_RESIZE)) {
            if (this.setWidth(stageW + stageX - eventX))
                this.stage.setX(eventX);
            this.setHeight(eventY - stageY);
        } else if (this.cursorEdge.equals(Cursor.E_RESIZE)) {
            this.setWidth(eventX - stageX);
        } else if (this.cursorEdge.equals(Cursor.S_RESIZE)) {
            this.setHeight(eventY - stageY);
        } else if (this.cursorEdge.equals(Cursor.W_RESIZE)) {
            if (this.setWidth(stageW + stageX - eventX))
                this.stage.setX(eventX);
        }
    }

}
