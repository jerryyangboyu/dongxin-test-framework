package pro.boyu.dongxin.framework.infobean;

import pro.boyu.dongxin.framework.annotations.TestMethod;

import java.lang.reflect.Method;

public class TestMethodInvokeInfo {
    Method method;
    TestMethod testMethod;
    Object[] args;
    Object target;
    public TestMethodInvokeInfo(Object obj,Method method,TestMethod testMethod,Object... args){
        this.method=method;
        this.testMethod=testMethod;
        this.args=args;
        this.target=obj;
    }
}
