package pro.boyu.dongxin.framework.executor;

import pro.boyu.dongxin.framework.infobean.TestExecuteInfo;
import pro.boyu.dongxin.framework.infobean.TestMethodInvokeInfo;
import pro.boyu.dongxin.utils.Subject;
import pro.boyu.dongxin.utils.iface.Observable;

public class AsyncTestMethodExecutor extends Thread{
    TestMethodInvokeInfo testMethodInvokeInfo;
    Object asyncLock;
    Subject<TestExecuteInfo> asyncSubject;
    public AsyncTestMethodExecutor(TestMethodInvokeInfo testMethodInvokeInfo,Object asyncLock){
        this.testMethodInvokeInfo=testMethodInvokeInfo;
        this.asyncLock=asyncLock;
    }
    public void run(){
    	
        synchronized (asyncLock){

            asyncLock.notify();
        }

    }
    public Observable<TestExecuteInfo> getObservable(){
       return null;
    }
}
