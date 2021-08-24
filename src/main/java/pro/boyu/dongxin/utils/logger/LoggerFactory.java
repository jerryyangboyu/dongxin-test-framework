package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.utils.logger.Logger;

public class LoggerFactory {
    public static boolean debug = false;

    public static Logger getLogger(Class<?> clazz) {
        return new Logger();
    }

    public static Logger getLogger(String name) {
        return new Logger();
    }

}
