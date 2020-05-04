package bfide.ide;

import bfide.ide.popups.AsciiPopup;
import bfide.ide.tabs.BfTab;
import bfide.ide.tabs.editor.EditorTab;
import bfide.ide.tabs.editor.EditorTabReadonly;
import bfide.ide.tabs.editor.InterpreterModel;
import bfide.ide.tabs.welcome.WelcomeTab;
import static bfide.splash.Splash.CSS_SPLASH_FADE;
import bfide.util.BfLogger;
import static bfide.util.Constants.FONT_AWESOME;
import static bfide.util.Constants.README_URL;
import bfide.util.MaximizeController;
import bfide.util.PropertiesState;
import bfide.util.StageControlBuilder;
import bfide.util.StageResizerBuilder;
import bfide.util.Util;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.CONTROL;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.Z;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class IDEController implements Initializable,
                                      PropertyChangeListener,
                                      MaximizeController {

    /**************************************************************************
     * Fields
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @FXML
    private StackPane root;

    private AsciiPopup asciiTablePopup;

    private FileChooser fileChooser;

    @FXML
    private HBox ribbon;

    /* Alerts */

    public Alert unsavedWorkAlert;

    private Alert openReadmeFailedAlert;

    private Alert executionRateAlert;

    /* Program Buttons */

    @FXML
    private Button iconifyButton;
    private Glyph ICONIFY_BUTTON_GRAPHIC;

    @FXML
    private Button maximizeButton;
    private Glyph MAXIMIZE_BUTTON_GRAPHIC_EXPAND;
    private Glyph MAXIMIZE_BUTTON_GRAPHIC_COMPRESS;

    @FXML
    private Button closeButton;
    private Glyph CLOSE_BUTTON_GRAPHIC;

    /* Menu File */

    @FXML
    private MenuItem menuFileNew;
    @FXML
    private MenuItem menuFileOpen;
    @FXML
    private MenuItem menuFileSave;
    @FXML
    private MenuItem menuFileSaveAs;
    @FXML
    private MenuItem menuFileCloseTab;
    @FXML
    private MenuItem menuFileQuit;

    /* Menu Visualizer */

    @FXML
    private Menu menuVisualizer;
    @FXML
    private CheckMenuItem menuVisualizerEnabled;

    private Slider menuVisualizerRateSlider;

    /* Menu View > Visualizer Settings */

    @FXML
    private Menu menuViewVisualizer;
    @FXML
    private CheckMenuItem menuViewVisualizerAll;
    @FXML
    private CheckMenuItem menuViewVisualizerEnabled;
    @FXML
    private CheckMenuItem menuViewVisualizerSetExecutionRate;

    /* Menu Help */

    @FXML
    private Menu menuHelp;

    @FXML
    private Menu menuHelpHowTo;
    private static final String EXAMPLES_DIR = "resources/examples/";

    /* Program Run Controls */

    @FXML
    private HBox interpreterControlsBox;
    @FXML
    private Button buttonPlayPause;
    @FXML
    private Button buttonStop;

    /* Visualizer Settings */

    @FXML
    private HBox visualizerSettingsBox;
    @FXML
    private CheckBox visualizerEnabled;
    @FXML
    private Slider executionRateSlider;

    /* Editor */

    @FXML
    private TabPane editorTabPane;

    private WelcomeTab welcomeTab;

    /* MaximizeController */

    private boolean isMaximized = false;
    private Rectangle2D unmaximizedState = Rectangle2D.EMPTY;

    // </editor-fold>

    /**************************************************************************
     * Initialization
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    /**
     * Initializes the content class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fadeIn();

        ICONIFY_BUTTON_GRAPHIC = FONT_AWESOME
            .create(FontAwesome.Glyph.MINUS)
            .size(18)
            .color(Color.WHITE);

        MAXIMIZE_BUTTON_GRAPHIC_EXPAND = FONT_AWESOME
            .create(FontAwesome.Glyph.EXPAND)
            .size(18)
            .color(Color.WHITE);

        MAXIMIZE_BUTTON_GRAPHIC_COMPRESS = FONT_AWESOME
            .create(FontAwesome.Glyph.COMPRESS)
            .size(18)
            .color(Color.WHITE);

        CLOSE_BUTTON_GRAPHIC = FONT_AWESOME
            .create(FontAwesome.Glyph.TIMES)
            .size(18)
            .color(Color.WHITE);

        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File(
            System.getProperty("user.home")
        ));

        this.asciiTablePopup = new AsciiPopup();

        this.welcomeTab = new WelcomeTab();

        this.setupListeners();

        this.setupUI();

        this.setupAlerts();
    }

    private void setupListeners() {
        this.root.sceneProperty().addListener((os, oldScene, newScene) ->
            this.onSceneChange(newScene));

        this.menuFileNew.setAccelerator(new KeyCodeCombination(
            KeyCode.N,
            KeyCombination.CONTROL_DOWN));
        this.menuFileOpen.setAccelerator(new KeyCodeCombination(
            KeyCode.O,
            KeyCombination.CONTROL_DOWN));
        this.menuFileSave.setAccelerator(new KeyCodeCombination(
            KeyCode.S,
            KeyCombination.CONTROL_DOWN));
        this.menuFileSaveAs.setAccelerator(new KeyCodeCombination(
            KeyCode.S,
            KeyCombination.SHIFT_DOWN,
            KeyCombination.CONTROL_DOWN));
        this.menuFileCloseTab.setAccelerator(new KeyCodeCombination(
            KeyCode.W,
            KeyCombination.CONTROL_DOWN));
        this.menuFileQuit.setAccelerator(new KeyCodeCombination(
            KeyCode.Q,
            KeyCombination.SHIFT_DOWN,
            KeyCombination.CONTROL_DOWN));

        // Update program execution rate while slider value is changing
        this.executionRateSlider.valueProperty().addListener(
            (ObservableValue<? extends Number> ov, Number o, Number value) -> {
                this.currentEditorTabDo((EditorTab tab) ->
                    tab.setExecutionRate(value));
                this.updateExecutionRateSliderTooltip();
            }
        );

        this.welcomeTab.setOnNewFile(e -> this.onNewFile());
        this.welcomeTab.setOnOpenFile(e -> this.onOpenFile());
        this.welcomeTab.setOnHowTo(e -> {
            this.menuHelp.show();
            this.menuHelpHowTo.show();
        });

        this.editorTabPane.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) -> {
                if (oldTab != null)
                    this.onLeaveTab((BfTab) oldTab);
                if (newTab != null)
                    this.onEnterTab((BfTab) newTab);
            }
        );
    }

    private void setupUI() {
        this.iconifyButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.iconifyButton.setGraphic(ICONIFY_BUTTON_GRAPHIC);

        this.maximizeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.maximizeButton.setGraphic(MAXIMIZE_BUTTON_GRAPHIC_EXPAND);

        this.closeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.closeButton.setGraphic(CLOSE_BUTTON_GRAPHIC);

        Util.bindManagedToVisible(this.interpreterControlsBox);

        Util.bindManagedToVisible(this.visualizerSettingsBox);
        Util.bindManagedToVisible(this.visualizerEnabled);
        Util.bindManagedToVisible(this.executionRateSlider);

        this.menuVisualizerEnabled.selectedProperty().bindBidirectional(
            this.visualizerEnabled.selectedProperty());

        // Populate Menu Help > How To
        this.forEachExampleFile((String name) -> {
            if (name.startsWith(EXAMPLES_DIR) && name.endsWith(".bf")) {
                MenuItem menuItem = new MenuItem(
                    name.substring(EXAMPLES_DIR.length()));
                menuItem.setOnAction(event ->
                    this.newEditorTabReadonly().openResource("/" + name));
                this.menuHelpHowTo.getItems().add(menuItem);
            }
        });

        this.updateExecutionRateSliderTooltip();

        this.editorTabPane.getTabs().add(this.welcomeTab);
    }

    private void setupAlerts() {
        /* Unsaved Work */

        this.unsavedWorkAlert = new Alert(
            Alert.AlertType.WARNING,
            "Do you want to quit without saving?",
            ButtonType.YES, ButtonType.NO);

        /* Open Readme Failed */

        this.openReadmeFailedAlert = new Alert(
            Alert.AlertType.WARNING,
            null,
            ButtonType.CLOSE);

        this.openReadmeFailedAlert.setHeaderText(null);

        Text text = new Text(
            "Failed to open the README in your default browser.");

        Button copyButton = new Button("Copy Url");
        copyButton.setContentDisplay(ContentDisplay.RIGHT);
        copyButton.setGraphicTextGap(8);
        copyButton.setGraphic(FONT_AWESOME.create(FontAwesome.Glyph.COPY));
        copyButton.setOnAction(event -> {
            ClipboardContent cc = new ClipboardContent();
            cc.putString(README_URL);
            Clipboard.getSystemClipboard().setContent(cc);

            Util.flashTooltipAboveNode(
                copyButton, Duration.millis(1200), "Copied!");
        });

        VBox aboutContent = new VBox(text, copyButton);
        aboutContent.setSpacing(12);

        this.openReadmeFailedAlert.getDialogPane().setContent(aboutContent);

        /* Execution Rate */

        this.executionRateAlert = new Alert(
            Alert.AlertType.NONE,
            null,
            ButtonType.OK, ButtonType.CANCEL);

        this.menuVisualizerRateSlider =
            Util.cloneSlider(this.executionRateSlider);

        TextField textField = new TextField();

        BiConsumer<Node, Number> updateValue = (source, value) -> {
            if (source.equals(this.menuVisualizerRateSlider)) {
                textField.setText(String.format("%.2f", value));
            } else {
                this.menuVisualizerRateSlider.setValue(value.doubleValue());
            }
        };

        this.menuVisualizerRateSlider.valueProperty().addListener(
            (ov, v, value) -> {
                if (this.menuVisualizerRateSlider.isValueChanging())
                    updateValue.accept(this.menuVisualizerRateSlider, value);
            }
        );

        textField.textProperty().addListener((ov, o, stringValue) -> {
            try {
                Double value = Double.valueOf(stringValue);
                updateValue.accept(textField, value);
            } catch (NumberFormatException ex) {
            }
        });

        updateValue.accept(
            this.menuVisualizerRateSlider,
            this.menuVisualizerRateSlider.getValue());

        VBox execRateContent = new VBox(
            this.menuVisualizerRateSlider,
            textField);
        execRateContent.setAlignment(Pos.TOP_CENTER);
        execRateContent.setSpacing(16);

        this.executionRateAlert.getDialogPane().setContent(execRateContent);
    }

    private void fadeIn() {
        Pane pane = new Pane();
        pane.getStyleClass().add(CSS_SPLASH_FADE);
        this.root.getChildren().add(pane);

        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(pane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setInterpolator(Interpolator.EASE_IN);
        transition.setOnFinished(e -> this.root.getChildren().remove(pane));
        transition.play();
    }

    // </editor-fold>

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    private Stage getStage() {
        return (Stage) this.root.getScene().getWindow();
    }

    private void onEnterTab(BfTab tab) {
        if (tab.interpreterSettingsState == null) {
            // Default state
            boolean isEditorTab = tab instanceof EditorTab;
            this.menuFileSave.setDisable(!isEditorTab);
            this.menuFileSaveAs.setDisable(!isEditorTab);
            this.menuViewVisualizer.setDisable(!isEditorTab);
            this.menuVisualizer.setDisable(!isEditorTab);
            this.interpreterControlsBox.setVisible(isEditorTab);
            this.visualizerSettingsBox.setVisible(isEditorTab);
            this.visualizerEnabled.setVisible(isEditorTab);
            this.executionRateSlider.setVisible(isEditorTab);
            this.executionRateSlider.setValue(this.executionRateSlider.getMin());
        } else {
            // Restore state
            tab.onEnter();
        }
    }

    private void onLeaveTab(BfTab tab) {
        // Set tab-specific properties to preserve
        tab.interpreterSettingsState = new PropertiesState(
            // Menu
            this.menuVisualizer.disableProperty(),
            this.menuViewVisualizer.disableProperty(),
            this.menuViewVisualizerAll.selectedProperty(),
            this.menuViewVisualizerEnabled.selectedProperty(),
            this.menuViewVisualizerSetExecutionRate.selectedProperty(),
            // Interpreter Controls
            this.interpreterControlsBox.visibleProperty(),
            this.buttonPlayPause.textProperty(),
            this.buttonStop.disableProperty(),
            // Visualizer Settings
            this.visualizerSettingsBox.visibleProperty(),
            this.visualizerEnabled.visibleProperty(),
            this.visualizerEnabled.selectedProperty(),
            this.visualizerEnabled.disableProperty(),
            this.executionRateSlider.visibleProperty(),
            this.executionRateSlider.valueProperty()
        );

        // Save state
        tab.onLeave();
    }

    private EditorTab newEditorTab() {
        EditorTab newTab = (EditorTab) this.newTab(EditorTab.class);

        // Set focus on TextArea
        newTab.requestFocus();

        return newTab;
    }

    private EditorTabReadonly newEditorTabReadonly() {
        return (EditorTabReadonly) this.newTab(EditorTabReadonly.class);
    }

    private BfTab newTab(Class<?> tabType) {
        BfTab newTab;
        try {
            newTab = (BfTab) tabType.getDeclaredConstructor(this.getClass())
                .newInstance(this);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }

        int newTabIndex = this.editorTabPane.getTabs().size();

        this.editorTabPane.getTabs().add(newTab);

        this.editorTabPane.getSelectionModel().select(newTabIndex);

        return newTab;
    }

    private void currentTabDo(Consumer<BfTab> consumer) {
        BfTab currentTab =
            (BfTab) this.editorTabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null)
            consumer.accept(currentTab);
    }

    private void currentEditorTabDo(Consumer<EditorTab> consumer) {
        this.currentTabDo((BfTab tab) -> {
            if (tab instanceof EditorTab)
                consumer.accept((EditorTab) tab);
        });
    }

    private void forEachExampleFile(Consumer<String> consumer) {
        // https://stackoverflow.com/a/28057735/5673922
        URL jar = this.getClass()
            .getProtectionDomain()
            .getCodeSource()
            .getLocation();
        try {
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry ze = zip.getNextEntry();
            for (; ze != null; ze = zip.getNextEntry())
                consumer.accept(ze.getName());
        } catch (IOException ex) {
            new BfLogger().logMethod("Error opening file: " + jar);
        }
    }

    private void updateExecutionRateSliderTooltip() {
        double rate = this.executionRateSlider.getValue();
        this.executionRateSlider.setTooltip(new Tooltip(
            String.format("x%.2f", rate)));
    }

    // </editor-fold>

    /**************************************************************************
     * Window Button Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @FXML
    public void onIconify() {
        // Undecorated windows cannot animate (de-)iconification :(
        this.getStage().setIconified(true);
    }

    @Override
    public boolean isMaximized() {
        return this.isMaximized;
    }

    @Override
    public void setMaximized(boolean value) {
        Stage stage = this.getStage();

        if (value) {
            // Prevent window from going out of screen
            double x = Math.max(stage.getX(), 0);
            double y = Math.max(stage.getY(), 0);
            // Save current position and size of stage
            this.unmaximizedState = new Rectangle2D(
                x, y, stage.getWidth(), stage.getHeight());

            // Maximize the stage, not covering the taskbar
            Util.currentScreenDo(stage, (Screen screen) -> {
                Rectangle2D bounds = screen.getVisualBounds();
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
            });

            // Update maximize button icon
            this.maximizeButton.setGraphic(MAXIMIZE_BUTTON_GRAPHIC_COMPRESS);
        } else {
            // Restore unmaximized state
            stage.setX(this.unmaximizedState.getMinX());
            stage.setY(this.unmaximizedState.getMinY());
            stage.setWidth(this.unmaximizedState.getWidth());
            stage.setHeight(this.unmaximizedState.getHeight());

            // Update maximize button icon
            this.maximizeButton.setGraphic(MAXIMIZE_BUTTON_GRAPHIC_EXPAND);
        }

        this.isMaximized = value;
    }

    @FXML
    @Override
    public void toggleMaximized() {
        this.setMaximized(!this.isMaximized);
    }

    @FXML
    public void onClose() {
        Stream<Tab> tabs = this.editorTabPane.getTabs().stream();
        if (tabs.anyMatch(tab -> ((BfTab) tab).isDirty())) {
            this.unsavedWorkAlert.setHeaderText(
                "Some files have been modified.");
            this.unsavedWorkAlert.showAndWait().ifPresent(ret -> {
                if (ret == ButtonType.YES)
                    this.getStage().close();
            });
        } else {
            this.getStage().close();
        }
    }

    // </editor-fold>

    /**************************************************************************
     * MenuBar Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @FXML
    public void onNewFile() {
        this.newEditorTab().setUntitled();
    }

    @FXML
    public void onOpenFile() {
        List<File> files =
            this.fileChooser.showOpenMultipleDialog(this.getStage());
        if (files != null)
            files.forEach((f) -> this.newEditorTab().openFile(f));
    }

    @FXML
    public void onSave() {
        this.currentEditorTabDo((EditorTab tab) -> tab.save());
    }

    @FXML
    public void onSaveAs() {
        this.currentEditorTabDo((EditorTab tab) -> tab.saveAs());
    }

    @FXML
    public void onCloseTab() {
        this.currentTabDo((BfTab tab) -> tab.tryClose());
    }

    @FXML
    public void onQuit() {
        this.onClose();
    }

    @FXML
    public void onVisualizerSetExecutionRate() {
        this.executionRateAlert.showAndWait().ifPresent(ret -> {
            if (ret == ButtonType.OK)
                this.executionRateSlider.setValue(
                    this.menuVisualizerRateSlider.getValue());
        });
    }

    @FXML
    public void onViewVisualizerSetting(ActionEvent event) {
        CheckMenuItem source = (CheckMenuItem) event.getSource();

        if (source.equals(this.menuViewVisualizerAll)) {
            // If the source was the All CheckBox update the rest to have the
            // same value
            boolean isSelected = this.menuViewVisualizerAll.isSelected();
            int i = 0;
            for (MenuItem setting : this.menuViewVisualizer.getItems()) {
                if (i++ > 0) {
                    ((CheckMenuItem) setting).setSelected(isSelected);
                    this.toggleVisualizerSetting((CheckMenuItem) setting);
                }
            }

        } else {
            // If the source was NOT the All CheckBox select the All CheckBox
            // iff all of the rest are selected
            int i = 0;
            boolean allSelected = true;
            for (MenuItem setting : this.menuViewVisualizer.getItems()) {
                if (i++ > 0 && !((CheckMenuItem) setting).isSelected()) {
                    allSelected = false;
                    break;
                }
            }
            this.menuViewVisualizerAll.setSelected(allSelected);
        }

        this.toggleVisualizerSetting(source);
    }

    private void toggleVisualizerSetting(CheckMenuItem source) {
        boolean isSelected = source.isSelected();

        // Skip over this if source == menuViewVisualizerSettingsAll
        if (source.equals(this.menuViewVisualizerEnabled))
            this.visualizerEnabled.setVisible(isSelected);
        else if (source.equals(this.menuViewVisualizerSetExecutionRate))
            this.executionRateSlider.setVisible(isSelected);

        // Hide visualizerSettingsBox if none of its children are visible
        int i = 0;
        boolean anySelected = false;
        for (Node setting : this.visualizerSettingsBox.getChildren()) {
            if (i++ > 0 && setting.isVisible()) {
                anySelected = true;
                break;
            }
        }
        this.visualizerSettingsBox.setVisible(anySelected);
    }

    @FXML
    public void onHelpAsciiTable() {
        this.asciiTablePopup.show(this.getStage());
    }

    @FXML
    public void onHelpAbout() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(README_URL));
            } catch (URISyntaxException ex) {
                new BfLogger().logMethod("Failed to open README: " + README_URL);
            } catch (IOException ex) {
                new BfLogger().logMethod("Failed to open README: " + README_URL);
            }
        } else {
            this.openReadmeFailedAlert.show();
        }
    }

    // </editor-fold>

    /**************************************************************************
     * Interpreter & Visualizer Control Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @FXML
    public void onButtonPlayPause() {
        this.currentEditorTabDo((EditorTab tab) -> tab.onPlayPause());
    }

    @FXML
    public void onButtonStop() {
        this.onStop();
        this.currentEditorTabDo((EditorTab tab) -> tab.onStop());
    }

    @FXML
    public void onVisualizerEnabled() {
        this.currentEditorTabDo(tab ->
            tab.setVisualizerVisible(this.visualizerEnabled.isSelected()));
    }

    // </editor-fold>

    /**************************************************************************
     * Event Handlers
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    private void onSceneChange(Scene newScene) {
        newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ALT)
                // Prevent focusing MenuBar when Alt pressed
                event.consume();
            else if (event.getCode() == KeyCode.Z
                  && event.isControlDown()
                  && event.isShiftDown())
                // Show Ascii Table on Ctrl+Shift+Z
                this.asciiTablePopup.show(this.getStage());
        });

        newScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case CONTROL: case SHIFT: case Z:
                    this.asciiTablePopup.hide();
                    break;
            }
        });

        newScene.windowProperty().addListener((ow, oldWindow, newWindow) ->
            this.onWindowChange(newWindow));
    }

    private void onWindowChange(Window newWindow) {
        // Setup window drag and resize listeners
        new StageControlBuilder((Stage) newWindow)
            .doubleClickToMaximize(this)
            .node(this.ribbon)
            .build();
        new StageResizerBuilder()
            .stage((Stage) newWindow)
            .build();
    }

    private void onStart() {
        this.buttonPlayPause.setText("Pause");
        this.buttonStop.setDisable(false);
        this.visualizerEnabled.setDisable(true);
    }

    private void onPause() {
        this.buttonPlayPause.setText("Play");
    }

    private void onPlay() {
        this.buttonPlayPause.setText("Pause");
    }

    private void onStop() {
        this.buttonPlayPause.setText("Play");
        this.buttonStop.setDisable(true);
        this.visualizerEnabled.setDisable(false);
    }

    // </editor-fold>

    /**************************************************************************
     * MVC Communication
     *************************************************************************/

    // <editor-fold defaultstate="collapsed">

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case InterpreterModel.START: this.onStart(); break;
            case InterpreterModel.PAUSE: this.onPause(); break;
            case InterpreterModel.PLAY:  this.onPlay();  break;
            case InterpreterModel.STOP:  this.onStop();  break;
        }
    }

    // </editor-fold>

}
