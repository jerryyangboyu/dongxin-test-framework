package com.uusafe.platform.test.common.utils.iface;

public interface Observer<T>{

    public void next(T data);

    public void error(Exception e);

    public void completed();
}
