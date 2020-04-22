package brainfuckide.ide.tabs.howto;

import brainfuckide.ide.IDEController;
import brainfuckide.ide.tabs.BfTab;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class HowToTab extends BfTab {

    private final HowToTabContent content;

    public HowToTab(IDEController controller) {
        super("How To");

        this.content = new HowToTabContent(controller);
        this.setContent(this.content);
    }

    @Override
    public Type getType() {
        return Type.HOW_TO;
    }

}
