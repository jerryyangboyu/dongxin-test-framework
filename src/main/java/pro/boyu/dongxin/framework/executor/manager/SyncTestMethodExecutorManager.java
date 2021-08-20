package pro.boyu.dongxin.framework.executor.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.IntFunction;

import pro.boyu.dongxin.framework.executor.SyncTestCaseExecutor;
import pro.boyu.dongxin.framework.executor.TestMethodObserver;
import pro.boyu.dongxin.framework.infobean.TestMethodInvokeInfo;

public class SyncTestMethodExecutorManager extends Thread{
	
	private Object lockObject;
	Map<Integer, List<SyncTestCaseExecutor>> syncTestCaseExecutors=new HashMap<>();
	public SyncTestMethodExecutorManager(Object targetObject) {
		
	}
	
	public void addMethod(TestMethodInvokeInfo testMethodInvokeInfo) {
		int level=testMethodInvokeInfo.getTestMethod().priority();
		if(!syncTestCaseExecutors.containsKey(level)) {
			syncTestCaseExecutors.put(level, new LinkedList<SyncTestCaseExecutor>());
		}
		syncTestCaseExecutors.get(level).add(new SyncTestCaseExecutor(testMethodInvokeInfo, lockObject));
		
	}
	
	
	public void exec() {
		Set<Integer> keys=this.syncTestCaseExecutors.keySet();
		Integer[] seqIntegers=keys.toArray((value)->{
			final int size=keys.size();
			Integer[] seq=new Integer[size];
			int count=0;
			for(var key :keys) {
				seq[count]=key;
				count++;
			}
			 Arrays.sort(seq);
			 return seq;
		});
		for(int key:seqIntegers) {
			List<SyncTestCaseExecutor> executors=this.syncTestCaseExecutors.get(keys);
			for(SyncTestCaseExecutor executor:executors) {
				TestMethodObserver observer=new TestMethodObserver(getName());
				observer.subscribe(executor.testMethodObervable());
				executor.start();
				observer.exportInfos();
			}
		}
	}
	
	public void run() {
		this.exec();
	}
	
	private void execTestMethods(int level) {
		
	}

}
