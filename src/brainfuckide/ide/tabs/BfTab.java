package brainfuckide.ide.tabs;

import brainfuckide.util.BfLogger;
import brainfuckide.util.PropertiesState;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public abstract class BfTab extends Tab {

    private static final String CSS_TABS_SHADOW = "editor-tabs-shadow";

    protected VBox root;

    protected String title;

    public PropertiesState interpreterSettingsState;

    public BfTab(String title) {
        super(title);

        Pane shadowBox = new Pane();
        shadowBox.getStyleClass().add(CSS_TABS_SHADOW);

        this.root = new VBox(shadowBox);
        super.setContent(this.root);
    }

    public void setBfContent(Node node) {
        VBox.setVgrow(node, Priority.ALWAYS);
        this.root.getChildren().add(node);
    }

    public void onEnter() {
        new BfLogger("tab").logMethod();
        if (this.interpreterSettingsState != null)
            this.interpreterSettingsState.restore();
    }

    public void onLeave() {
        new BfLogger("tab").logMethod();
        if (this.interpreterSettingsState != null)
            this.interpreterSettingsState.save();
    }

    public enum Type {
        WELCOME,
        EDITOR,
        HOW_TO
    }

    public abstract Type getType();

    public boolean tryClose() {
        new BfLogger("tab").logMethod();

        TabPaneSkin skin = (TabPaneSkin) super.getTabPane().getSkin();
        TabPaneBehavior behavior = skin.getBehavior();

        if (behavior.canCloseTab(this) == false)
            return false;

        behavior.closeTab(this);
        return true;
    }

}
