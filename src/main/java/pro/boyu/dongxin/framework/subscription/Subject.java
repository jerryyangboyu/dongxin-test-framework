package pro.boyu.dongxin.framework.subscription;

import pro.boyu.dongxin.utils.iface.Observable;
import pro.boyu.dongxin.utils.iface.Observer;
import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Subject<T> implements Observable<T> {
    protected final String idString = UUID.randomUUID().toString();

    protected final List<Observer<T>> observers = new LinkedList<>();

    public String getId() {
        return this.idString;
    }

    public synchronized void updateData(T data) {
        this.data = data;
        this.infoAll();
    }

    protected T data;

    public synchronized void infoAll() {
        for (Observer<T> observer : observers) {
            observer.next(data);
        }
    }

    public synchronized void pushError(Exception e) {
        for (Observer<T> observer : observers) {
            observer.error(e);
        }
    }


    protected synchronized void completedSuccess() {

    }

    protected synchronized void completedError(Exception e) {

    }

    protected synchronized List<Observer<T>> getObservers() {
        return observers;
    }

    @Override
    public synchronized Unsubscribtion subscribe(Observer<T> observer) {
        this.add(observer);
        if (null != data) {
            observer.next(data);
        }
        return new Unsubscribtion() {
            @Override
            public void unsubscribe() {
                Subject.this.remove(observer);
            }
        };
    }


    protected synchronized void remove(Observer<T> observer) {
        Subject.this.observers.removeIf(currentObserver -> currentObserver == observer);
    }


    protected synchronized void add(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public String name() {
        return idString;
    }

}
