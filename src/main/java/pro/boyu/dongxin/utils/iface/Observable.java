package pro.boyu.dongxin.utils.iface;

import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;

public interface Observable<T> {
    public Unsubscribtion subscribe(Observer<T> observer);

    public void remove(Observer<T> observer);

    public void add(Observer<T> observer);

    public String name();
}
