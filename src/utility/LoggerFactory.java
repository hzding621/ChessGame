package utility;

import java.util.logging.Logger;

/**
 * Created by haozhending on 9/3/16.
 */
public final class LoggerFactory {

    private LoggerFactory() {
        // Utils
    }

    public static Logger getInstance(Class clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
