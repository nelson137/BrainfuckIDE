package bfide.ide.tabs.welcome;

import bfide.ide.tabs.BfTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeTab extends BfTab {

    private final WelcomeTabContent content;

    public WelcomeTab() {
        super("Welcome");

        this.content = new WelcomeTabContent();
        super.setBfContent(this.content);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    public void setOnNewFile(EventHandler<ActionEvent> handler) {
        this.content.setOnNewFile(handler);
    }

    public void setOnOpenFile(EventHandler<ActionEvent> handler) {
        this.content.setOnOpenFile(handler);
    }

    public MenuButton getHowToMenuButton() {
        return this.content.getHowToMenuButton();
    }

}
