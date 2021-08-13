package com.uusafe.platform.test.common.bean;

import com.uusafe.platform.test.common.bean.annotations.TestMethod;

import java.lang.reflect.Method;

public class TestMethodInvokeInfo {
    Method method;
    TestMethod testMethod;
    Object[] args;
    public TestMethodInvokeInfo(Method method,TestMethod testMethod,Object... args){
        this.method=method;
        this.testMethod=testMethod;
        this.args=args;
    }
}
