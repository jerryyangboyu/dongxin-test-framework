package com.uusafe.platform.test.common;

import com.uusafe.platform.test.common.bean.TestExecuteResult;
import com.uusafe.platform.test.common.bean.TestMethodInvokeInfo;
import com.uusafe.platform.test.common.bean.annotations.TestMethod;
import com.uusafe.platform.test.common.utils.Subject;
import com.uusafe.platform.test.common.utils.iface.Observable;

public class AsyncTestMethodExecutor extends Thread{

    TestMethodInvokeInfo testMethodInvokeInfo;
    Object asyncLock;
    public AsyncTestMethodExecutor(TestMethodInvokeInfo testMethodInvokeInfo,Object asyncLock){
        this.testMethodInvokeInfo=testMethodInvokeInfo;
        this.asyncLock=asyncLock;
    }
    public void run(){

        synchronized (asyncLock){

            asyncLock.notify();
        }

    }
    public Observable<TestExecuteResult> getObservable(){
        return new Subject<TestExecuteResult>(){

        };
    }
}
