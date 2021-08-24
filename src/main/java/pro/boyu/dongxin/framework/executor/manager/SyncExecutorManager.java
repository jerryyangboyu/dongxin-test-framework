package pro.boyu.dongxin.framework.executor.manager;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.executor.SyncExecutor;
import pro.boyu.dongxin.framework.subscription.ExecutionObserver;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;

import java.util.*;

public class SyncExecutorManager extends Thread{
	
	private final Object lockObject = new Object();
	private final Map<Integer, List<SyncExecutor>> syncTestCaseExecutors = new HashMap<>();

	public void addMethod(MethodExecutionInfo methodExecutionInfo) {
		int level= methodExecutionInfo.getTestMethod().priority();
		if(!syncTestCaseExecutors.containsKey(level)) {
			syncTestCaseExecutors.put(level, new LinkedList<SyncExecutor>());
		}
		syncTestCaseExecutors.get(level).add(new SyncExecutor(methodExecutionInfo, lockObject));
	}
	
	
	public void exec() {
		Set<Integer> keys = this.syncTestCaseExecutors.keySet();
		keys.stream().sorted().forEach((Integer key) -> {
			List<SyncExecutor> executors=this.syncTestCaseExecutors.get(key);
			for(SyncExecutor executor:executors) {
				synchronized (this.lockObject){
					ExecutionObserver observer=new ExecutionObserver();
					observer.subscribe(executor.testMethodObservable());
					executor.start();
					try {
						this.lockObject.wait(executor.maxWait());
					} catch (InterruptedException e) {
						ExecutionInfo info = new ExecutionInfo(System.currentTimeMillis(), TestCaseState.ERRORFIN, "ERROR: TIMEOUT");
						observer.next(info);
					} finally {
						observer.exportInfos();
					}
				}
			}
		});
	}
	
	public void run() {
		this.exec();
	}
	
	private void execTestMethods(int level) {
		
	}

}
