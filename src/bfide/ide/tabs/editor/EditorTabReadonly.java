package bfide.ide.tabs.editor;

import bfide.ide.IDE;
import static bfide.util.Constants.FONT_AWESOME;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class EditorTabReadonly extends EditorTab {

    private boolean readonlyWarningShown;

    public EditorTabReadonly(IDE controller) {
        super(controller);
        this.readonlyWarningShown = false;
    }

    @Override
    protected void updateDirtyIndicator() {
        super.setText(/*"[RO] " + */this.title);
        super.setGraphic(FONT_AWESOME
            .create(FontAwesome.Glyph.EYE)
            .color(Color.WHITE));
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void save() {
        this.showReadonlyWarning();
    }

    @Override
    public void saveAs() {
        this.showReadonlyWarning();
    }

    private void showReadonlyWarning() {
        if (this.readonlyWarningShown)
            return;

        final String header = "This file is readonly.";
        final String message =
            "If you would like to save your modifications to this file " +
            "you must copy & paste the contents into another tab.";

        Alert alert = new Alert(
            Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(header);
        alert.show();

        this.readonlyWarningShown = true;
    }

}
