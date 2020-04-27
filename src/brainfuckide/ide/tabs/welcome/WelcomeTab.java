package brainfuckide.ide.tabs.welcome;

import brainfuckide.ide.tabs.BfTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class WelcomeTab extends BfTab {

    private WelcomeContent content;

    public WelcomeTab() {
        super("Welcome");

        this.content = new WelcomeContent();
        super.setBfContent(this.content);
    }

    @Override
    public Type getType() {
        return Type.WELCOME;
    }

    public void setOnNewFile(EventHandler<ActionEvent> handler) {
        this.content.setOnNewFile(handler);
    }

    public void setOnOpenFile(EventHandler<ActionEvent> handler) {
        this.content.setOnOpenFile(handler);
    }

}
