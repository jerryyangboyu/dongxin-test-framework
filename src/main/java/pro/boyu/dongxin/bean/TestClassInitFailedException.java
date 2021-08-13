package com.uusafe.platform.test.common.bean;

public class TestClassInitFailedException extends Exception{
    public TestClassInitFailedException(String className,String message){
        super("Test Case "+className+" init Failed ,Because of "+message);
    }
}
