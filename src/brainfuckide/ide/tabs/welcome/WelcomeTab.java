package brainfuckide.ide.tabs.welcome;

import brainfuckide.ide.tabs.BfTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeTab extends BfTab {

    private WelcomeTabContent content;

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

    public void setOnHowTo(EventHandler<MouseEvent> handler) {
        this.content.setOnHowTo(handler);
    }

}
