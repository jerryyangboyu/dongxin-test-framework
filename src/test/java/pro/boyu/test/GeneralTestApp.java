package pro.boyu.test;

import pro.boyu.dongxin.framework.TestApplication;
import pro.boyu.dongxin.framework.subscription.ExecutionObserver;
import pro.boyu.dongxin.utils.logger.LoggerFactory;
import pro.boyu.dongxin.utils.logger.OutputInfoBean;

import java.util.List;
import java.util.Map;

public class GeneralTestApp {
    public static void main(String[] args) {
        TestApplication.run(GeneralTestApp.class);
        Map<String, List<OutputInfoBean>> map = LoggerFactory.getMap();
        List<OutputInfoBean> outputInfoBeans = map.get(ExecutionObserver.class.getName());
        System.out.println(outputInfoBeans);
    }
}
