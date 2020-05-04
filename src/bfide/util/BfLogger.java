package bfide.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class BfLogger {

    private static final Logger LOGGER = Logger.getLogger("");

    static {
        // Use custom log handler:
        // https://stackoverflow.com/a/2963751/5673922
        LOGGER.setUseParentHandlers(false);
        for (Handler pHandler : LOGGER.getHandlers())
            LOGGER.removeHandler(pHandler);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new BfLoggerFormatter());
        LOGGER.addHandler(handler);
    }

    private static HashSet<String> ACTIVE_TAGS = new HashSet();

    public static void setActiveTags(String... tags) {
        ACTIVE_TAGS = new HashSet(Arrays.asList(tags));
    }

    private final HashSet<String> tags;

    private Level level = Level.INFO;
    private String className = null;
    private String methodName = null;
    private String message = null;

    public BfLogger(String... tags) {
        this.tags = new HashSet(Arrays.asList(tags));
    }

    public BfLogger level(Level level) {
        this.level = level;
        return this;
    }

    public BfLogger className(String name) {
        this.className = name;
        return this;
    }

    public BfLogger methodName(String name) {
        this.methodName = name;
        return this;
    }

    private boolean shouldLog() {
        return this.tags.isEmpty()
            || this.tags.stream().anyMatch(t -> ACTIVE_TAGS.contains(t));
    }

    public void logMethod() {
        this.logMethodImpl();
    }

    public void logMethod(String message) {
        this.message = message;
        this.logMethodImpl();
    }

    private void logMethodImpl() {
        if (this.shouldLog() == false)
            return;

        // Get information about calling method:
        // https://www.geeksforgeeks.org/get-name-of-current-method-being-executed-in-java/
        StackTraceElement target = Thread.currentThread().getStackTrace()[3];

        if (this.className == null) {
            // Get simple class name from fully-qualified
            String name = target.getClassName();
            String[] parts = name.split("\\.");
            this.className = parts.length > 1
                ? parts[parts.length - 1]
                : parts[0];
        }

        if (this.methodName == null)
            this.methodName = target.getMethodName();

        StringBuilder messageBuilder = new StringBuilder();

        // Add tags if there are any
        if (this.tags.isEmpty() == false)
            messageBuilder
                .append("[")
                .append(String.join(",", this.tags))
                .append("] ");

        // Add calling class and method
        messageBuilder
            .append(this.className)
            .append(".")
            .append(this.methodName)
            .append("()");

        // Add message if there is one
        if (message != null)
            messageBuilder
                .append(" : ")
                .append(this.message);

        LOGGER.log(this.level, messageBuilder.toString());
    }

}
