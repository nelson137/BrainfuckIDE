package brainfuckide.ide.tabs.editor;

import brainfuckide.ide.IDEController;
import brainfuckide.ide.tabs.BfTab;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Paths;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public final class EditorTab extends BfTab {

    private static int NUM_UNTITLED_TABS = 0;

    public EditorTabContent content;

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
        this.setContent(this.content);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if (this.content.inputPromptIsVisible())
            this.content.forwardFocusToInputPromptCursor();
    }

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    public void onButtonPlayPause() {
        switch (this.content.getStatus()) {
            case RUNNING: this.content.pause();    break;
            case PAUSED:  this.content.play();     break;
            case STOPPED: this.content.startNew(); break;
        }
    }

    public void onButtonStop() {
        this.content.stop();
    }

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

    private void updateDirtyIndicator() {
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
    public Type getType() {
        return Type.EDITOR;
    }

    public boolean isDirty() {
        String currentText = this.content.getEditorText();

        if (this.textOnLastSave == null)
            return currentText.length() != 0;

        return currentText.equals(this.textOnLastSave) == false;

        /*
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diff =
            dmp.diff_main(this.textOnLastSave, currentText);
        System.out.println("diff : " + diff);

        return diff.size() != 1
            || diff.getFirst().operation != diff_match_patch.Operation.EQUAL;
        */
    }

    private void alertError(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(exception.getMessage());

        // Create expandable Exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(new VBox(label, textArea));

        alert.showAndWait();
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
            this.alertError(ex);
        }
    }

    private void openFile(Reader reader) {
        StringBuilder document = new StringBuilder();
        BufferedReader buffReader = null;

        try {
            buffReader = new BufferedReader(reader);
            buffReader.lines().forEach(
                line -> document.append(line).append('\n'));
            buffReader.close();

            String text = document.toString();
            this.textOnLastSave = text;
            this.content.setEditorText(text);
        } catch (IOException ex) {
            this.alertError(ex);
        } finally {
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException ex) {
                    this.alertError(ex);
                }
            }
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
            this.alertError(ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    this.alertError(ex);
                }
            }
        }
    }

}
