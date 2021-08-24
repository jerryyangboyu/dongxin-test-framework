package pro.boyu.dongxin.framework.subscription;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.subscription.TestSubject;

public class ExecutorSubject extends TestSubject {
    public ExecutorSubject(String className, String methodName) {
        super(className, methodName);
    }

    public synchronized void completedError(Exception e) {
        this.getObservers().forEach((observer)->{
            observer.next(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.ERRORFIN, e.getMessage()));
        });
    }

    public synchronized void completedSuccess() {
        this.getObservers().forEach(o->{
            o.next(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.SUCCESSFIN));
            //o.completed();
        });
    }
}