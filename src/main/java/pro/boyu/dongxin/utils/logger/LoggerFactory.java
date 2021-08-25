package pro.boyu.dongxin.utils.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggerFactory {
    public static boolean debug = false;

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }

    public static Map<String, List<OutputInfoBean>> getMap() {
        return LogCache.getCache().exportMap();
    }

}
