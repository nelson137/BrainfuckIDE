package brainfuckide.util;

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
    private MaximizeController maximizeController;
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

    public StageControlBuilder doubleClickToMaximize(MaximizeController controller) {
        this.doubleClickCanMaximize = true;
        this.maximizeController = controller;
        return this;
    }

    @Override
    protected void onMouseClick(MouseEvent event) {
        super.onMouseClick(event);
        if (this.doubleClickCanMaximize
         && event.getButton().equals(this.doubleClickButton)
         && event.getClickCount() == 2
         && event.isStillSincePress())
            this.maximizeController.toggleMaximized();
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
            if (this.maximizeController.isMaximized()) {
                this.maximizeController.setMaximized(false);
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
                this.maximizeController.setMaximized(true);
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

        Util.currentScreenDo(this.stage, (Screen screen) -> {
            Rectangle2D bounds = screen.getVisualBounds();
            this.root.setPrefSize(bounds.getMaxX(), bounds.getMaxY());
            this.maximizeIndicator.setWidth(bounds.getMaxX());
            this.maximizeIndicator.setHeight(bounds.getMaxY());
            this.maximizeIndicator.show(
                this.stage,
                bounds.getMinX(), bounds.getMinY());
        });
    }

}
