package utility;

import java.util.logging.Logger;

/**
 * Factory class used to create Loggers
 * TODO: tune this class
 */
public final class LoggerFactory {

    private LoggerFactory() {
        // Utils
    }

    public static Logger getInstance(Class clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
