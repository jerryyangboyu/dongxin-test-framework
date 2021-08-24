package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.executor.AsyncExecutor;
import pro.boyu.dongxin.framework.executor.SyncExecutor;

public class TestClassManager {
    SyncExecutor syncExecutor;
    AsyncExecutor asyncExecutor;
    Object stepLock=new Object();
    public TestClassManager(Object testClazzInstance){

    }
}
