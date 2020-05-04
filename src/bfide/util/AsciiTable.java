package bfide.util;

import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public interface AsciiTable {

    public static final List<List<Pair<String, String>>> ASCII_TABLE = Arrays.asList(

        // Column 1
        Arrays.asList(
            new Pair("NUL", "null"),
            new Pair("SOH", "start of heading"),
            new Pair("STX", "start of text"),
            new Pair("ETX", "end of text"),
            new Pair("EOT", "end of transmission"),
            new Pair("ENQ", "enquiry"),
            new Pair("ACK", "acknowledge"),
            new Pair("BEL", "bell"),
            new Pair("BS", "backspace"),
            new Pair("TAB", "horizontal tab"),
            new Pair("LF", "line feed (\\n)"),
            new Pair("VT", "vertical tab"),
            new Pair("FF", "new page"),
            new Pair("CR", "carriage return (\\r)"),
            new Pair("SO", "shift out"),
            new Pair("SI", "shift in")
        ),

        // Column 2
        Arrays.asList(
            new Pair("DLE", "data link escape"),
            new Pair("DC1", "device control 1"),
            new Pair("DC2", "device control 2"),
            new Pair("DC3", "device control 3"),
            new Pair("DC4", "device control 4"),
            new Pair("NAK", "negative ACK"),
            new Pair("SYN", "synchronous idle"),
            new Pair("ETB", "end of transmit block"),
            new Pair("CAN", "cancel"),
            new Pair("EM", "end of medium"),
            new Pair("SUB", "substitute"),
            new Pair("ESC", "escape"),
            new Pair("FS", "file separator"),
            new Pair("GS", "group separator"),
            new Pair("RS", "record separator"),
            new Pair("US", "unit separator")
        ),

        // Column 3
        Arrays.asList(
            new Pair("SPACE", ""),
            new Pair("!", ""),
            new Pair("\"", ""),
            new Pair("#", ""),
            new Pair("$", ""),
            new Pair("%", ""),
            new Pair("&", ""),
            new Pair("'", ""),
            new Pair("(", ""),
            new Pair(")", ""),
            new Pair("*", ""),
            new Pair("+", ""),
            new Pair(",", ""),
            new Pair("-", ""),
            new Pair(".", ""),
            new Pair("/", "")
        ),

        // Column 4
        Arrays.asList(
            new Pair("0", ""),
            new Pair("1", ""),
            new Pair("2", ""),
            new Pair("3", ""),
            new Pair("4", ""),
            new Pair("5", ""),
            new Pair("6", ""),
            new Pair("7", ""),
            new Pair("8", ""),
            new Pair("9", ""),
            new Pair(":", ""),
            new Pair(";", ""),
            new Pair("<", ""),
            new Pair("=", ""),
            new Pair(">", ""),
            new Pair("?", "")
        ),

        // Column 5
        Arrays.asList(
            new Pair("@", ""),
            new Pair("A", ""),
            new Pair("B", ""),
            new Pair("C", ""),
            new Pair("D", ""),
            new Pair("E", ""),
            new Pair("F", ""),
            new Pair("G", ""),
            new Pair("H", ""),
            new Pair("I", ""),
            new Pair("J", ""),
            new Pair("K", ""),
            new Pair("L", ""),
            new Pair("M", ""),
            new Pair("N", ""),
            new Pair("O", "")
        ),

        // Column 6
        Arrays.asList(
            new Pair("P", ""),
            new Pair("Q", ""),
            new Pair("R", ""),
            new Pair("S", ""),
            new Pair("T", ""),
            new Pair("U", ""),
            new Pair("V", ""),
            new Pair("W", ""),
            new Pair("X", ""),
            new Pair("Y", ""),
            new Pair("Z", ""),
            new Pair("[", ""),
            new Pair("\\", ""),
            new Pair("]", ""),
            new Pair("^", ""),
            new Pair("_", "")
        ),

        // Column 7
        Arrays.asList(
            new Pair("`", ""),
            new Pair("a", ""),
            new Pair("b", ""),
            new Pair("c", ""),
            new Pair("d", ""),
            new Pair("e", ""),
            new Pair("f", ""),
            new Pair("g", ""),
            new Pair("h", ""),
            new Pair("i", ""),
            new Pair("j", ""),
            new Pair("k", ""),
            new Pair("l", ""),
            new Pair("m", ""),
            new Pair("n", ""),
            new Pair("o", "")
        ),

        // Column 8
        Arrays.asList(
            new Pair("p", ""),
            new Pair("q", ""),
            new Pair("r", ""),
            new Pair("s", ""),
            new Pair("t", ""),
            new Pair("u", ""),
            new Pair("v", ""),
            new Pair("w", ""),
            new Pair("x", ""),
            new Pair("y", ""),
            new Pair("z", ""),
            new Pair("{", ""),
            new Pair("|", ""),
            new Pair("}", ""),
            new Pair("~", ""),
            new Pair("DEL", "")
        )

    );

}
