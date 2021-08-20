package pro.boyu.dongxin.framework.infobean;

import pro.boyu.dongxin.framework.annotations.TestMethod;

import java.lang.reflect.Method;

public class TestMethodInvokeInfo {
   private Method method;
   private TestMethod testMethod;
   private Object[] args;
   private Object target;
    public TestMethodInvokeInfo(Object obj,Method method,TestMethod testMethod,Object... args){
        this.method=method;
        this.testMethod=testMethod;
        this.args=args;
        this.target=obj;
    }
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public TestMethod getTestMethod() {
		return testMethod;
	}
	public void setTestMethod(TestMethod testMethod) {
		this.testMethod = testMethod;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
}
