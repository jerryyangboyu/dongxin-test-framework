package pro.boyu.dongxin.utils.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggerFactory {
    public static boolean debug = false;

    private static final List<Logger> loggers = new ArrayList<>();
    private static final Map<String, List<OutputInfoBean>> map = new HashMap<>();

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        Logger logger = new Logger(name);
        loggers.add(logger);
        return logger;
    }

    public static Map<String, List<OutputInfoBean>> getMap() {
        for (Logger logger: loggers) {
            map.put(logger.getId(), logger.getInfo());
        }
        return map;
    }

}
