package brainfuckide.ide.popups;

import static brainfuckide.util.Constants.ASCII_TABLE;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class AsciiGridPane extends GridPane {

    private static final String CSS_CELL = "ascii-table-cell";
    private static final String CSS_BORDER = "ascii-table-cell-border";
    private static final String CSS_CODE = "ascii-table-cell-code";
    private static final String CSS_SYMBOL = "ascii-table-cell-symbol";
    private static final String CSS_DESC = "ascii-table-cell-description";

    public AsciiGridPane() {
        super();

        Iterator<List<Pair<String,String>>> colIter;
        int r, i = 0;
        HBox cell;
        String desc;

        for (colIter = ASCII_TABLE.iterator(); colIter.hasNext(); ) {
            List<Pair<String,String>> col = colIter.next();
            r = 0;

            for (Pair<String,String> value : col) {
                cell = new HBox(
                    this.makeGridCell(String.valueOf(i++), CSS_CODE),
                    this.makeGridCell(value.getKey(), CSS_SYMBOL)
                );

                desc = value.getValue();
                if (desc.length() > 0)
                    cell.getChildren().add(this.makeGridCell(desc, CSS_DESC));

                super.addRow(r, cell);

                if (colIter.hasNext())
                    super.addRow(r, this.makeBorder());

                r++;
            }
        }
    }

    private StackPane makeGridCell(String text, String cssClass) {
        StackPane p = new StackPane(new Label(text));
        p.getStyleClass().addAll(CSS_CELL, cssClass);
        return p;
    }

    private Pane makeBorder() {
        Pane p = new Pane();
        p.getStyleClass().add(CSS_BORDER);
        return p;
    }

}
