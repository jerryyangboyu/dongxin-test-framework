package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


import pro.boyu.dongxin.framework.annotations.CaseSubject;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.framework.subscription.ExecutorSubject;
import pro.boyu.dongxin.utils.iface.Observable;

public abstract class Executor extends Thread {
	protected Method method;
	protected Object targetObject;
	protected Object[] args;
	protected TestMethod testMethod;
	protected Class<?> testClass;
	protected ExecutorSubject subject;

	public Executor(MethodExecutionInfo info) {
		this.method = info.getMethod();
		this.targetObject = info.getTarget();
		this.args = info.getArgs();
		this.testMethod= info.getTestMethod();
		this.testClass = info.getClazz();
		this.subject = new ExecutorSubject(info.getClassName(), info.getMethodName());
		this.injectCaseSubject();
	}

	protected void injectCaseSubject() {
		Parameter[] parameters=this.method.getParameters();
		for(int index=0;index<parameters.length;index++) {
			if(parameters[index].isAnnotationPresent(CaseSubject.class)) {
				this.args[index]=this.subject;
			}
		}
	}

	public ExecutorSubject getObservable() {
		return subject;
	}

	public long maxWait(){
		return this.testMethod.maxTime();
	}
}
