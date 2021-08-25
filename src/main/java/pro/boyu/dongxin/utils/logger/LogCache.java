package pro.boyu.dongxin.utils.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogCache {
    private final Map<String, List<OutputInfoBean>> map = new HashMap<>();

    static LogCache cache = new LogCache();

    private LogCache() {}

    public static LogCache getCache() {
        return cache;
    }

    public void addListInfos(String name, List<OutputInfoBean> beans) {
        map.put(name, beans);
    }

    public void appendInfo(String name, OutputInfoBean bean) {
        map.computeIfAbsent(name, k -> new ArrayList<>());
        map.get(name).add(bean);
    }

    public Map<String, List<OutputInfoBean>> exportMap() {
        return this.map;
    }

}
