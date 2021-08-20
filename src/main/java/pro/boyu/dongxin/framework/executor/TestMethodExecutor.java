package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Method;


import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.infobean.TestExecuteInfo;
import pro.boyu.dongxin.framework.infobean.TestMethodInvokeInfo;
import pro.boyu.dongxin.utils.Subject;
import pro.boyu.dongxin.utils.iface.Observable;

public abstract class TestMethodExecutor extends Thread {
	private Method method;
	private Object targetObject;
	private Object[] args;
	TestMethod testMethod;
	private Subject<TestExecuteInfo> subject;

	public TestMethodExecutor(TestMethodInvokeInfo testMethodInvokeInfo) {
		this.method = testMethodInvokeInfo.getMethod();
		this.targetObject = testMethodInvokeInfo.getTarget();
		this.args = testMethodInvokeInfo.getArgs();
		this.testMethod=testMethodInvokeInfo.getTestMethod();
	}
	
	
	public abstract Observable<TestExecuteInfo> testMethodObervable();
}
