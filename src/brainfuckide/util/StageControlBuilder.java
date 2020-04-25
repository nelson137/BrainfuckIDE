package brainfuckide.util;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.PopupControl;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class StageControlBuilder extends WindowControlBuilder {

    private final Stage stage;

    private boolean doubleClickCanMaximize = false;
    private MouseButton doubleClickButton = MouseButton.PRIMARY;
    private boolean doMaximizeOnRelease = false;

    private static final String CSS_INDICATOR = "maximize-indicator";
    private static final String CSS_INDICATOR_INNER = "maximize-indicator-inner";

    private PopupControl maximizeIndicator;
    private StackPane root;
    private Pane rootInner;

    public StageControlBuilder(Stage stage) {
        super();
        this.stage = stage;

        this.maximizeIndicator = new PopupControl();

        this.rootInner = new Pane();
        this.rootInner.getStyleClass().add(CSS_INDICATOR_INNER);

        this.root = new StackPane(rootInner);
        this.root.getStyleClass().add(CSS_INDICATOR);
        this.maximizeIndicator.getScene().setRoot(this.root);
    }

    @Override
    public Stage getWindow() {
        return this.stage;
    }

    public StageControlBuilder doubleClickToMaximize() {
        this.doubleClickCanMaximize = true;
        return this;
    }

    @Override
    protected void onMouseClick(MouseEvent event) {
        super.onMouseClick(event);
        if (this.doubleClickCanMaximize
         && event.getButton().equals(this.doubleClickButton)
         && event.getClickCount() == 2
         && event.isStillSincePress())
            this.stage.setMaximized(!this.stage.isMaximized());
    }

    @Override
    protected void onMousePress(MouseEvent event) {
        super.onMousePress(event);
        if (event.isPrimaryButtonDown())
            // Make sure this gets reset every time primary is clicked
            this.doMaximizeOnRelease = false;
    }

    @Override
    protected void onMouseDrag(MouseEvent event) {
        super.onMouseDrag(event);
        if (event.isPrimaryButtonDown()) {
            if (this.stage.isMaximized()) {
                this.stage.setMaximized(false);
                // Adjust x offset so that the window gets horizontally
                // centered on the cursor
                this.dragOffset.x = this.stage.getWidth() / 2;
            } else {
                this.doMaximizeOnRelease = event.getScreenY() == 0;
                this.willMaximizeOnReleaseIndicator();
            }
        }
    }

    @Override
    protected void onMouseRelease(MouseEvent event) {
        super.onMouseRelease(event);
        if (event.isPrimaryButtonDown() == false) {
            if (this.doMaximizeOnRelease) {
                this.getEventStage(event).setMaximized(true);
                this.doMaximizeOnRelease = false;
                this.willMaximizeOnReleaseIndicator();
            }
        }
    }

    private void willMaximizeOnReleaseIndicator() {
        if (this.doMaximizeOnRelease == false) {
            this.maximizeIndicator.hide();
            return;
        }

        if (this.maximizeIndicator.isShowing())
            return;

        this.currentScreenDo(screen -> {
            Rectangle2D bounds = screen.getBounds();
            this.root.setPrefSize(bounds.getMaxX(), bounds.getMaxY());
            this.maximizeIndicator.setWidth(bounds.getMaxX());
            this.maximizeIndicator.setHeight(bounds.getMaxY());
            this.maximizeIndicator.show(
                this.stage,
                bounds.getMinX(), bounds.getMinY());
        });
    }

    private interface ScreenAction {
        void doAction(Screen screen);
    }

    private void currentScreenDo(ScreenAction action) {
        ObservableList<Screen> screens = Screen.getScreensForRectangle(
            this.stage.getX(), this.stage.getY(),
            this.stage.getWidth(), this.stage.getHeight());
        if (screens.size() > 0)
            action.doAction(screens.get(0));
    }

}
