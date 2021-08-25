package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.utils.logger.LoggerFactory;

public class TestApplication {
    public static void run(Class<?> c) {
        LoggerFactory.debug = false;
        OrderedPackageLoader.run(c);
        System.out.println(LoggerFactory.getMap());
    }
}
