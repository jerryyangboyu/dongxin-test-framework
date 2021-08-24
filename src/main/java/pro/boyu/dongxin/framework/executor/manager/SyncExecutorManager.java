package pro.boyu.dongxin.framework.executor.manager;

import pro.boyu.dongxin.concurrent.ParallelExecutors;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.executor.Executor;
import pro.boyu.dongxin.framework.executor.SimpleExecutor;
import pro.boyu.dongxin.framework.subscription.ExecutionObserver;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;

import java.util.*;

public class SyncExecutorManager extends Thread{
	
	private final Object lockObject = new Object();

	protected final Map<Integer, List<Executor>> syncTestCaseExecutors = new HashMap<>();

	public void addMethod(MethodExecutionInfo info) {
		int level= info.getTestMethod().priority();
		if(!syncTestCaseExecutors.containsKey(level)) {
			syncTestCaseExecutors.put(level, new LinkedList<>());
		}
		if (info.getTestMethod().threadsNum() > 1) {
			syncTestCaseExecutors.get(level).add(new ParallelExecutors(info));
		} else {
			syncTestCaseExecutors.get(level).add(new SimpleExecutor(info, lockObject));
		}
	}
	
	
	public void exec() {
		Set<Integer> keys = this.syncTestCaseExecutors.keySet();
		keys.stream().sorted().forEach((Integer key) -> {
			List<Executor> executors=this.syncTestCaseExecutors.get(key);
			for(Executor executor:executors) {
				synchronized (this.lockObject){
					ExecutionObserver observer=new ExecutionObserver();
					observer.subscribe(executor.getObservable());
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

}
