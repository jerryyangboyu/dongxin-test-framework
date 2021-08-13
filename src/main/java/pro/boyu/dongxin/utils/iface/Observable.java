package com.uusafe.platform.test.common.utils.iface;

import com.uusafe.platform.test.common.utils.jucservice.Unsubscribtion;

public interface Observable<T> {
    public Unsubscribtion subscribe(Observer<T> observer);

    public void remove(Observer<T> observer);

    public void add(Observer<T> observer);

    public String name();
}
