package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.executor.AsyncTestMethodExecutor;
import pro.boyu.dongxin.framework.executor.SyncTestCaseExecutor;

public class TestClassManager {
    SyncTestCaseExecutor syncTestCaseExecutor;
    AsyncTestMethodExecutor asyncTestMethodExecutor;
    Object stepLock=new Object();
    public TestClassManager(Object testClazzInstance){

    }
}
