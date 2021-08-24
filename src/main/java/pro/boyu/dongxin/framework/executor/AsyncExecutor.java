package pro.boyu.dongxin.framework.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.framework.subscription.Subject;
import pro.boyu.dongxin.utils.iface.Observable;

public class AsyncExecutor extends Thread{
    MethodExecutionInfo methodExecutionInfo;
    Object asyncLock;
    Subject<ExecutionInfo> asyncSubject;
    public AsyncExecutor(MethodExecutionInfo methodExecutionInfo, Object asyncLock){
        this.methodExecutionInfo = methodExecutionInfo;
        this.asyncLock=asyncLock;
    }
    public void run(){
    	 ScheduledExecutorService killService = Executors.newSingleThreadScheduledExecutor();
    	

    }
    public Observable<ExecutionInfo> getObservable(){
       return null;
    }
}
