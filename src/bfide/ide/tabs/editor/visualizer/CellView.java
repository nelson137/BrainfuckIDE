package bfide.ide.tabs.editor.visualizer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Nelson Earle (nwewnh)
 */
public class CellView extends VBox implements Initializable {

    private static final String FXML_PATH = "CellView.fxml";

    private static final String CSS_HIGHLIGHT = "visualizer-cell-highlight";

    @FXML
    private StackPane cellPane;

    @FXML
    private Label cellLabel;

    @FXML
    private Label asciiLabel;

    public CellView() {
        super();

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
    public void initialize(URL location, ResourceBundle rb) {
        this.cellLabel.textProperty().addListener(
            (ObservableValue<? extends String> ov, String o, String value) -> {
                this.onCellChange(value);
            }
        );
    }

    private void onCellChange(String stringValue) {
        String text = "";
        try {
            int value = Integer.parseInt(stringValue);
            switch (value) {
                case  0: text = "\\0"; break;
                case  9: text = "\\t"; break;
                case 10: text = "\\n"; break;
                case 13: text = "\\r"; break;
                case 32: text = "' '"; break;
                default:
                    if (33 <= value && value <= 126)
                        // '!' <= value <= '~'
                        text = String.valueOf((char) value);
                    break;
            }
        } catch (NumberFormatException nfe) {
        }
        this.asciiLabel.setText(text);
    }

    public void setHighlighted(boolean value) {
        if (value)
            this.cellPane.getStyleClass().add(CSS_HIGHLIGHT);
        else
            this.cellPane.getStyleClass().remove(CSS_HIGHLIGHT);
    }

    public void setValue(Integer value) {
        this.cellLabel.setText(value.toString());
    }

}
