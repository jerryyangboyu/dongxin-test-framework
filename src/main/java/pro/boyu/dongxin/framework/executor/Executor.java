package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Method;


import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.utils.iface.Observable;

public abstract class Executor extends Thread {
	protected Method method;
	protected Object targetObject;
	protected Object[] args;
	protected TestMethod testMethod;
	protected Class<?> testClass;

	public Executor(MethodExecutionInfo methodExecutionInfo) {
		this.method = methodExecutionInfo.getMethod();
		this.targetObject = methodExecutionInfo.getTarget();
		this.args = methodExecutionInfo.getArgs();
		this.testMethod= methodExecutionInfo.getTestMethod();
		this.testClass = methodExecutionInfo.getClazz();
	}

	public abstract Observable<ExecutionInfo> testMethodObservable();
}
