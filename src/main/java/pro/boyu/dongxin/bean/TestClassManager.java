package com.uusafe.platform.test.common.bean;

import com.uusafe.platform.test.common.AsyncTestMethodExecutor;

public class TestClassManager {
    SyncTestCaseExecutor syncTestCaseExecutor;
    AsyncTestMethodExecutor asyncTestMethodExecutor;
    Object stepLock=new Object();
    public TestClassManager(Object testClazzInstance){

    }
}
