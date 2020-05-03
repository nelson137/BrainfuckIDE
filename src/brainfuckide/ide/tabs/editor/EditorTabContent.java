package brainfuckide.ide.tabs.editor;

import brainfuckide.ide.IDEController;
import brainfuckide.ide.tabs.editor.spinner.BfSpinner;
import brainfuckide.ide.tabs.editor.visualizer.Visualizer;
import brainfuckide.util.BfLogger;
import brainfuckide.util.Util;
import static brainfuckide.util.Util.FONT_AWESOME;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class EditorTabContent
        extends SplitPane
        implements Initializable, PropertyChangeListener {

    /**************************************************************************
     * Fields
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    private static final String FXML_PATH = "EditorTabContent.fxml";

    private final IDEController controller;

    @FXML
    private TextArea textArea;

    private InterpreterModel interpreterModel;

    @FXML
    public Visualizer visualizerController;

    @FXML
    private TextArea output;

    @FXML
    private BfSpinner spinnerController;

    @FXML
    private HBox inputPrompt;
    @FXML
    private TextField inputPromptCursor;
    private static final String SPECIAL_CHARS = "`~!@#$%^&*()-_+=[]{}\\|;:'\",<.>/?";
    private FadeTransition inputPromptAnimation;

    @FXML
    private Button inputButton;
    private Alert inputAlert;
    private TextArea inputTextArea;
    private FileChooser inputFileChooser;
    private String programInput = "";

    // </editor-fold>

    /**************************************************************************
     * Initialization
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public EditorTabContent(IDEController controller) {
        super();

        this.controller = controller;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource(FXML_PATH));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.interpreterModel = new InterpreterModel();
        this.interpreterModel.addAllPropertyChangeListeners(
            this.controller,
            this,
            this.visualizerController
        );

        this.inputFileChooser = new FileChooser();
        this.inputFileChooser.setInitialDirectory(new File(
            System.getProperty("user.home")));

        this.setupListeners();

        this.setupUI();

        this.setupAnimations();

        this.setupAlerts();
    }

    private void setupListeners() {
        // Force focus to stay on inputPromptCursor while isVisible
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.inputPrompt.isVisible()) {
                this.inputPromptCursor.requestFocus();
                event.consume();
            }
        });

        // Forward TAB KeyEvent to onProgramInput instead of newTab traversal
        this.inputPromptCursor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB)
                this.onProgramInput(event);
        });
    }

    private void setupUI() {
        Util.bindManagedToVisible(this.inputButton);
        this.inputButton.toFront();
        this.inputButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.inputButton.setGraphic(FONT_AWESOME.create(
            FontAwesome.Glyph.KEYBOARD_ALT)
                .color(Color.WHITE)
                .size(24));

        this.inputPrompt.toFront();
        Util.bindManagedToVisible(this.inputPrompt);
    }

    private void setupAnimations() {
        this.inputPromptAnimation = new FadeTransition(
            Duration.millis(150),
            this.inputPrompt
        );
    }

    private void setupAlerts() {
        this.inputAlert = new Alert(
            Alert.AlertType.NONE,
            null,
            ButtonType.OK, ButtonType.CANCEL);
        this.inputAlert.setTitle("Program Input");

        this.inputAlert.setHeaderText(
            "Enter input here or choose a file to use as input:");

        this.inputAlert.setOnCloseRequest(event -> {
            this.inputAlert.close();
        });

        this.inputTextArea = new TextArea();

        Button openFileButton = new Button("Choose File");
        openFileButton.setOnAction(event -> {
            File file = this.inputFileChooser.showOpenDialog(
                this.inputAlert.getOwner());
            if (file == null)
                return;
            try {
                this.inputTextArea.setText(Util.readFile(file));
            } catch (IOException ex) {
                Util.flashTooltipAboveNode(
                    openFileButton,
                    Duration.millis(3000),
                    ex.getLocalizedMessage());
            }
        });

        VBox content = new VBox(this.inputTextArea, openFileButton);
        content.setSpacing(8);

        this.inputAlert.getDialogPane().setContent(content);
    }

    // </editor-fold>

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public Stage getStage() {
        return (Stage) this.textArea.getScene().getWindow();
    }

    public StringProperty getTextAreaTextProperty() {
        return this.textArea.textProperty();
    }

    public String getEditorText() {
        return this.textArea.getText();
    }

    @Override
    public void requestFocus() {
        this.textArea.requestFocus();
    }

    public void setEditorText(String text) {
        this.textArea.setText(text);
    }

    // </editor-fold>

    /**************************************************************************
     * Interpreter Control Methods
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    public double getExecutionRate() {
        return this.interpreterModel.getRate();
    }

    public void setExecutionRate(Number value) {
        this.interpreterModel.setExecutionRate(value);
        this.visualizerController.setExecutionRate(value);
    }

    public Status getStatus() {
        return this.interpreterModel.getStatus();
    }

    public boolean inputPromptIsVisible() {
        return this.inputPrompt.isVisible();
    }

    private void inputPromptSetVisible(boolean value) {
        if (value)
            this.inputPrompt.setVisible(true);

        this.inputPromptAnimation.setFromValue(value ? 0 : 1);
        this.inputPromptAnimation.setToValue(value ? 1 : 0);
        this.inputPromptAnimation.setOnFinished(e -> {
            if (value)
                this.inputPromptCursor.requestFocus();
            else
                this.inputPrompt.setVisible(false);
        });
        this.inputPromptAnimation.play();
    }

    public void startNew() {
        new BfLogger("interpreter").logMethod();
        // Reset
        this.output.clear();
        this.visualizerController.resetVisualizer();
        // Setup
        this.inputButton.setVisible(false);
        // Start interpreter
        this.interpreterModel.startNewInterpreter(
            this.textArea.getText(),
            this.programInput);
        // Start spinnerController
        this.spinnerController.start();
    }

    public void play() {
        new BfLogger("interpreter").logMethod();
        this.interpreterModel.playInterpreter();
        this.spinnerController.start();
    }

    public void pause() {
        new BfLogger("interpreter").logMethod();
        this.interpreterModel.pauseInterpreter();
        this.inputPromptSetVisible(false);
        this.spinnerController.stop();
    }

    public void stop() {
        new BfLogger("interpreter").logMethod();
        this.interpreterModel.stopInterpreter();
        this.inputPromptSetVisible(false);
        this.spinnerController.stop();
        this.inputButton.setVisible(true);
    }

    // </editor-fold>

    /**************************************************************************
     * Event Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @FXML
    public void forwardFocusToInputPromptCursor() {
        this.inputPromptCursor.requestFocus();
    }

    @FXML
    public void onProgramInput(KeyEvent event) {
        String value = event.getCharacter();
        char c = value.charAt(0);
        if (Character.isLetterOrDigit(c)
         || Character.isWhitespace(c)
         || SPECIAL_CHARS.contains(value)) {
            this.inputPromptSetVisible(false);
            this.interpreterModel.pushInput(c);
        }

        // Prevent event bubbling so text doesn't appear in inputPromptCursor
        event.consume();
    }

    @FXML
    public void onInputButton() {
        this.inputAlert.showAndWait().ifPresent(ret -> {
            if (ret.equals(ButtonType.OK))
                // Update program input
                this.programInput = this.inputTextArea.getText();
            else
                // Reset input TextArea back to the current program input
                this.inputTextArea.setText(this.programInput);
        });
    }

    // </editor-fold>

    /**************************************************************************
     * MVC Communication
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Object newValue = event.getNewValue();

        switch (event.getPropertyName()) {
            case InterpreterModel.PRINT_CHAR:
                this.output.appendText(newValue.toString());
                break;
            case InterpreterModel.READ_CHAR:
                this.inputPromptSetVisible(true);
                break;
            case InterpreterModel.FINISH:
                this.stop();
        }
    }

    // </editor-fold>

}
