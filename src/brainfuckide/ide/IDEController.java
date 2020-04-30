package brainfuckide.ide;

import brainfuckide.ide.popups.AsciiPopup;
import brainfuckide.ide.tabs.BfTab;
import brainfuckide.ide.tabs.editor.EditorTab;
import brainfuckide.ide.tabs.editor.interpreter.InterpreterModel;
import brainfuckide.ide.tabs.howto.HowToTab;
import brainfuckide.ide.tabs.welcome.WelcomeTab;
import static brainfuckide.splash.Splash.CSS_SPLASH_FADE;
import brainfuckide.util.BfLogger;
import brainfuckide.util.ui.MaximizeController;
import brainfuckide.util.PropertiesState;
import brainfuckide.util.ui.StageControlBuilder;
import brainfuckide.util.ui.StageResizerBuilder;
import brainfuckide.util.Util;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import static javafx.scene.input.KeyCode.N;
import static javafx.scene.input.KeyCode.O;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class IDEController implements Initializable,
                                      PropertyChangeListener,
                                      MaximizeController {

    @FXML
    private StackPane root;

    private AsciiPopup asciiTablePopup;

    private FileChooser fileChooser;

    private static final GlyphFont FONT_AWESOME =
        GlyphFontRegistry.font("FontAwesome");

    @FXML
    private HBox ribbon;

    /* Program Buttons */

    @FXML
    private Button iconifyButton;
    private final Glyph ICONIFY_BUTTON_GRAPHIC = FONT_AWESOME
        .create(FontAwesome.Glyph.MINUS)
        .size(18)
        .color(Color.WHITE);

    @FXML
    private Button maximizeButton;
    private final Glyph MAXIMIZE_BUTTON_GRAPHIC_EXPAND = FONT_AWESOME
        .create(FontAwesome.Glyph.EXPAND)
        .size(18)
        .color(Color.WHITE);
    private final Glyph MAXIMIZE_BUTTON_GRAPHIC_COMPRESS = FONT_AWESOME
        .create(FontAwesome.Glyph.COMPRESS)
        .size(18)
        .color(Color.WHITE);

    @FXML
    private Button closeButton;
    private final Glyph CLOSE_BUTTON_GRAPHIC = FONT_AWESOME
        .create(FontAwesome.Glyph.TIMES)
        .size(18)
        .color(Color.WHITE);

    /* Menu File */

    @FXML
    private MenuItem menuFileSave;
    @FXML
    private MenuItem menuFileSaveAs;

    /* Menu View > Visualizer Settings */

    @FXML
    private Menu menuViewVisualizerSettings;
    @FXML
    private CheckMenuItem menuViewVisualizerSettingsAll;
    @FXML
    private CheckMenuItem menuViewVisualizerSettingsEnabled;
    @FXML
    private MenuItem menuViewVisualizerSettingsSetExecutionRate;

    /* Menu Visualizer */

    @FXML
    private Menu menuVisualizer;
    @FXML
    private CheckMenuItem menuVisualizerEnabled;
    @FXML
    private MenuItem menuVisualizerSetExecutionRate;

    /* Program Run Controls */

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

    /**************************************************************************
     * Initialization
     *************************************************************************/

    /**
     * Initializes the content class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Pane fadePane = new Pane();
        fadePane.getStyleClass().add(CSS_SPLASH_FADE);
        this.root.getChildren().add(fadePane);

        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File(
            System.getProperty("user.home")
        ));

        this.welcomeTab = new WelcomeTab();

        this.setupListeners();

        this.setupUI();

        this.fadeOutPane(fadePane);
    }

    private void setupListeners() {
        Platform.runLater(() ->
            new StageControlBuilder((Stage) this.getStage())
                .doubleClickToMaximize(this)
                .node(this.ribbon)
                .build());

        Platform.runLater(() ->
            new StageResizerBuilder()
                .stage((Stage) this.getStage())
                .build());

        // KeyEvent listeners for menu options
        this.root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {

                    // New File
                    case N: this.onNewFile(); event.consume(); break;

                    // Open File
                    case O: this.onOpenFile(); event.consume(); break;

                    // Save, Save As
                    case S:
                        if (event.isShiftDown())
                            this.onSaveAs();
                        else
                            this.onSave();
                        event.consume();
                        break;

                    // Close Tab
                    case W: this.onCloseTab(); event.consume(); break;

                }
            }
        });

        // Update program execution rate while slider value is changing
        this.executionRateSlider.valueProperty().addListener(
            (ObservableValue<? extends Number> ov, Number o, Number value) -> {
                this.currentEditorTabDo(
                    (EditorTab tab) -> tab.content.setExecutionRate(value)
                );
                this.setExecutionRateSliderTooltip();
            }
        );

        this.welcomeTab.setOnNewFile(e -> this.onNewFile());
        this.welcomeTab.setOnOpenFile(e -> this.onOpenFile());
        this.welcomeTab.setOnHowTo(e -> this.onHowToBrainfuck());

        // Show ASCII Popup while Shift+Ctrl+Alt is down
        this.root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isShiftDown()
             && event.isControlDown()
             && event.isAltDown()) {
                this.asciiTablePopup.show(this.getStage());
            }
        });
        this.root.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.isShiftDown() == false
             || event.isControlDown() == false
             || event.isAltDown() == false) {
                this.asciiTablePopup.hide();
            }
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
        this.asciiTablePopup = new AsciiPopup();

        this.iconifyButton.setText(null);
        this.iconifyButton.setGraphic(ICONIFY_BUTTON_GRAPHIC);

        this.maximizeButton.setText(null);
        this.maximizeButton.setGraphic(MAXIMIZE_BUTTON_GRAPHIC_EXPAND);

        this.closeButton.setText(null);
        this.closeButton.setGraphic(CLOSE_BUTTON_GRAPHIC);

        Util.bindManagedToVisible(this.visualizerEnabled);
        Util.bindManagedToVisible(this.executionRateSlider);

        this.menuVisualizerEnabled.selectedProperty().bindBidirectional(
            this.visualizerEnabled.selectedProperty());

        Util.bindManagedToVisible(this.visualizerSettingsBox);

        this.setExecutionRateSliderTooltip();

        this.editorTabPane.getTabs().add(this.welcomeTab);
    }

    private void fadeOutPane(Pane pane) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setInterpolator(Interpolator.EASE_IN);
        fadeTransition.setOnFinished(event ->
            this.root.getChildren().remove(pane));
        fadeTransition.play();
    }

    /**************************************************************************
     * Utility Methods
     *************************************************************************/

    private Window getStage() {
        return this.root.getScene().getWindow();
    }

    private void onEnterTab(BfTab tab) {
        BooleanProperty properties[] = new BooleanProperty[] {
            this.menuFileSave.disableProperty(),
            this.menuFileSaveAs.disableProperty(),
            this.menuViewVisualizerSettings.disableProperty(),
            this.menuVisualizer.disableProperty()
        };
        boolean shouldDisable = tab.getType() != BfTab.Type.EDITOR;
        for (BooleanProperty p : properties)
            p.set(shouldDisable);

        tab.onEnter();
    }

    private void onLeaveTab(BfTab tab) {
        // Set tab-specific properties to preserve
        tab.interpreterSettingsState = new PropertiesState(
            this.buttonPlayPause.textProperty(),
            this.buttonStop.disableProperty(),
            this.visualizerEnabled.selectedProperty(),
            this.visualizerEnabled.disableProperty(),
            this.executionRateSlider.valueProperty()
        );

        // Save state
        tab.onLeave();
    }

    private BfTab getCurrentTab() {
        return (BfTab) this.editorTabPane.getSelectionModel().getSelectedItem();
    }

    private interface TabAction {
        void doAction(EditorTab tab);
    }

    private void currentEditorTabDo(TabAction action) {
        BfTab currentTab = this.getCurrentTab();
        if (currentTab.getType() == BfTab.Type.EDITOR)
            action.doAction((EditorTab) currentTab);
    }

    private void toggleVisualizerSetting(CheckMenuItem source) {
        boolean isSelected = source.isSelected();

        // Skip over this if source == menuViewVisualizerSettingsAll
        if (source.equals(this.menuViewVisualizerSettingsEnabled))
            this.visualizerEnabled.setVisible(isSelected);
        else if (source.equals(this.menuViewVisualizerSettingsSetExecutionRate))
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

    private void setExecutionRateSliderTooltip() {
        double rate = this.executionRateSlider.getValue();
        this.executionRateSlider.setTooltip(new Tooltip(
            String.format("x%.2f", rate)));
    }

    public EditorTab newEditorTab() {
        int newTabIndex = this.editorTabPane.getTabs().size();

        EditorTab newTab = new EditorTab(this);

        // Add the newTab to the view
        this.editorTabPane.getTabs().add(newTab);

        // Switch to the new newTab
        this.editorTabPane.getSelectionModel().select(newTabIndex);

        // Set focus on TextArea
        newTab.content.requestFocus();

        return newTab;
    }

    /**************************************************************************
     * Event Handlers
     *************************************************************************/

    @FXML
    public void onClose() {
        Platform.exit();
    }

    @Override
    public boolean isMaximized() {
        return this.isMaximized;
    }

    @Override
    public void setMaximized(boolean value) {
        Stage stage = (Stage) this.getStage();

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
    public void onIconify() {
        Stage stage = (Stage) this.getStage();
        // Undecorated windows cannot animate (de-)iconification :(
        stage.setIconified(true);
    }

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
        this.getCurrentTab().tryClose();
    }

    @FXML
    public void onExit() {
        this.editorTabPane.getTabs().stream()
            .map((Tab tab) -> (BfTab) tab)
            .filter((BfTab tab) -> tab.equals(this.welcomeTab) == false)
            .forEach((BfTab tab) -> ((EditorTab) tab).save());
    }

    @FXML
    public void onVisualizerEnabled() {
        this.currentEditorTabDo(
            (EditorTab tab) -> tab.content.visualizerController.setVisible(
                this.visualizerEnabled.isSelected()
            )
        );
    }

    @FXML
    public void onVisualizerSetExecutionRate() {
        new BfLogger().logMethod();
    }

    @FXML
    public void onViewVisualizerSetting(ActionEvent event) {
        CheckMenuItem source = (CheckMenuItem) event.getSource();

        if (source.equals(this.menuViewVisualizerSettingsAll)) {
            // If the source was the All CheckBox update the rest to have the
            // same value
            boolean isSelected = this.menuViewVisualizerSettingsAll.isSelected();
            int i = 0;
            for (MenuItem setting : this.menuViewVisualizerSettings.getItems()) {
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
            for (MenuItem setting : this.menuViewVisualizerSettings.getItems()) {
                if (i++ > 0 && !((CheckMenuItem) setting).isSelected()) {
                    allSelected = false;
                    break;
                }
            }
            this.menuViewVisualizerSettingsAll.setSelected(allSelected);
        }

        this.toggleVisualizerSetting(source);
    }

    @FXML
    public void onHelpAsciiTable() {
        this.asciiTablePopup.show(this.getStage());
    }

    @FXML
    public void onHelpAbout() {
        new BfLogger().logMethod();
    }

    @FXML
    public void onButtonPlayPause() {
        this.currentEditorTabDo((EditorTab tab) -> tab.onButtonPlayPause());
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

    private void onFinish() {
        this.buttonPlayPause.setText("Play");
        this.buttonStop.setDisable(true);
        this.visualizerEnabled.setDisable(false);
    }

    @FXML
    public void onButtonStop() {
        this.onFinish();
        this.currentEditorTabDo((EditorTab tab) -> tab.onButtonStop());
    }

    @FXML
    public void onHowToBrainfuck() {
        this.editorTabPane.getTabs().add(new HowToTab(this));
        this.editorTabPane.getSelectionModel().selectLast();
    }

    /**************************************************************************
     * MVC Communication
     *************************************************************************/

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case InterpreterModel.START:  this.onStart();  break;
            case InterpreterModel.PAUSE:  this.onPause();  break;
            case InterpreterModel.PLAY:   this.onPlay();   break;
            case InterpreterModel.FINISH: this.onFinish(); break;
        }
    }

}
