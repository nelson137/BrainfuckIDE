package bfide.ide.tabs.editor;

import bfide.ide.IDEController;
import bfide.ide.tabs.BfTab;
import bfide.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class EditorTab extends BfTab {

    /**************************************************************************
     * Fields & Constructor
     *************************************************************************/

    private static int NUM_UNTITLED_TABS = 0;

    private EditorTabContent content;

    private enum FileType {
        FILE,
        RESOURCE
    }

    private boolean hasFile = false;
    private FileType fileType;
    private File file;
    private File parentDirectory = null;
    private String textOnLastSave = null;

    public EditorTab(IDEController controller) {
        super(null);

        this.content = new EditorTabContent(controller);
        this.content.getTextAreaTextProperty().addListener((
            ObservableValue<? extends String> observableValue,
            String oldValue,
            String newValue
        ) -> {
            this.updateDirtyIndicator();
        });
        super.setBfContent(this.content);

        super.setOnCloseRequest(event -> {
            if (this.isDirty()) {
                controller.unsavedWorkAlert.setHeaderText(
                    "This file has unsaved changes.");
                controller.unsavedWorkAlert.showAndWait().ifPresent(ret -> {
                    if (ret != ButtonType.YES)
                        event.consume();
                });
            }
        });
    }

    /**************************************************************************
     * BfTab
     *************************************************************************/

    @Override
    public void onEnter() {
        super.onEnter();
        if (this.content.isInputPromptVisible())
            this.content.forwardFocusToInputPromptCursor();
    }

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    private String getUserHome() {
        return System.getProperty("user.home");
    }

    private void assignFile(File file) {
        this.hasFile = true;
        this.fileType = FileType.FILE;
        this.file = file;
        this.parentDirectory = file.getParentFile();
        this.title = file.getName();
        super.setText(this.title);
        super.setTooltip(new Tooltip(file.getAbsolutePath()));
    }

    private void assignResource(String path) {
        String parentDir = this.getUserHome();
        String basename = new File(path).getName();
        this.assignFile(Paths.get(parentDir, basename).toFile());
        this.fileType = FileType.RESOURCE;
    }

    protected void updateDirtyIndicator() {
        super.setText(this.isDirty() ? "• " + this.title : this.title);
    }

    public void setUntitled() {
        this.setTitle("Untitled " + ++NUM_UNTITLED_TABS);
    }

    private void setTitle(String name) {
        this.title = name;
        this.updateDirtyIndicator();
    }

    public File getParentDirectory() {
        if (this.parentDirectory == null || this.fileType == FileType.RESOURCE)
            return new File(this.getUserHome());
        return this.parentDirectory;
    }

    @Override
    public boolean isDirty() {
        String currentText = this.content.getEditorText();

        if (this.textOnLastSave == null)
            return currentText.length() != 0;

        return currentText.equals(this.textOnLastSave) == false;
    }

    /**************************************************************************
     * File Methods
     *************************************************************************/

    public void openResource(String path) {
        this.assignResource(path);

        this.openFile(new InputStreamReader(
            this.getClass().getResourceAsStream(path)));
    }

    public void openFile(File file) {
        this.assignFile(file);

        try {
            this.openFile(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Util.alertError(ex);
        }
    }

    private void openFile(Reader reader) {
        try {
            String text = Util.readFile(reader);
            this.textOnLastSave = text;
            this.content.setEditorText(text);
        } catch (IOException ex) {
            Util.alertError(ex);
        }
    }

    public void save() {
        if (this.hasFile)
            this.writeToFile();
        else
            this.saveAs();
    }

    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(this.getParentDirectory());
        fileChooser.setInitialFileName(this.title);
        File file = fileChooser.showSaveDialog(this.content.getStage());
        if (file != null) {
            this.assignFile(file);
            this.writeToFile();
        }
    }

    private void writeToFile() {
        if (this.hasFile == false || this.fileType != FileType.FILE)
            return;

        FileWriter writer = null;

        try {
            writer = new FileWriter(this.file);
            writer.write(this.content.getEditorText());

            this.textOnLastSave = this.content.getEditorText();
            this.updateDirtyIndicator();
        } catch (IOException ex) {
            Util.alertError(ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    Util.alertError(ex);
                }
            }
        }
    }

    /**************************************************************************
     * Visualizer Control
     *************************************************************************/

    public void onPlayPause() {
        switch (this.content.getStatus()) {
            case RUNNING: this.content.pause();    break;
            case PAUSED:  this.content.play();     break;
            case STOPPED: this.content.startNew(); break;
        }
    }

    public void onStop() {
        this.content.stop();
    }

    /**************************************************************************
     * Pass through to content
     *************************************************************************/

    public void setExecutionRate(Number value) {
        this.content.setExecutionRate(value);
    }

    public void requestFocus() {
        this.content.requestFocus();
    }

    public void setVisualizerVisible(boolean value) {
        this.content.setVisualizerVisible(value);
    }

}
