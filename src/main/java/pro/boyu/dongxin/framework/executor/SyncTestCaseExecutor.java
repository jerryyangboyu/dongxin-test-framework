package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Method;

import pro.boyu.dongxin.utils.Subject;

public class SyncTestCaseExecutor extends Thread  {
	private Method m;
	private Object[] args;
	private Object target;
	
	private Subject<String> subject=new Subject<String>();
	protected SyncTestCaseExecutor(Method m,Object target,Object... args){
		this.args=args;
		this.m=m;
		this.target=target;
	}

}