package brainfuckide.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom log formatter:
 * https://stackoverflow.com/a/2951641/5673922
 * @author Nelson Earle (nwewnh)
 */
public class BfLoggerFormatter extends Formatter {

    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");

    @Override
    public String format(final LogRecord record) {
        StringBuilder msg = new StringBuilder();
        msg.append('[');
            msg.append(record.getLevel());
        msg.append("] [");
            msg.append(DATE_FORMAT.format(new Date(record.getMillis())));
        msg.append("] ");
        msg.append(record.getMessage());
        msg.append('\n');
        return msg.toString();
    }

}
