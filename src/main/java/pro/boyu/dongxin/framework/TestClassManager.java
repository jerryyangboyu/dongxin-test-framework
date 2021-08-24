package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.concurrent.RepeatedThread;
import pro.boyu.dongxin.framework.executor.SimpleExecutor;

public class TestClassManager {
    SimpleExecutor simpleExecutor;
    RepeatedThread repeatedThread;
    Object stepLock=new Object();
    public TestClassManager(Object testClazzInstance){

    }
}
