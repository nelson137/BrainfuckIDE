package bfide.util;

import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public interface Constants {

    String README_URL =
        "https://github.com/nelson137/BrainfuckIDE/blob/master/README.md";

    String SPLASH_FXML = "/bfide/splash/Splash.fxml";
    String MAIN_FXML = "/bfide/ide/IDE.fxml";

    String CSS_STYLE = "/resources/css/style.css";
    String CSS_THEME_DARK = "/resources/css/dark-theme.css";

    String TITLE = "Brainfuck IDE";

    String WELCOME_FONT = "/resources/fonts/MuseoSans_500.otf";

    String ICON = "/resources/images/icon.png";

    GlyphFont FONT_AWESOME = GlyphFontRegistry.font("FontAwesome");

}
