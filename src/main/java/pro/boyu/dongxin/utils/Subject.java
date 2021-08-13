package pro.boyu.dongxin.utils;

import pro.boyu.dongxin.utils.iface.Observable;
import pro.boyu.dongxin.utils.iface.Observer;
import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Subject<T> implements Observable<T> {
    private List<Observer<T>> observers=new LinkedList<Observer<T>>();
    protected void updateData(T data){
        this.data=data;
    }
    private T data;
    public synchronized void infoAll(){
        for(Observer<T> observer:observers){
            observer.next(data);
        }
    }

    public synchronized void pushError(Exception e){
        for(Observer<T> observer:observers){
            observer.error(e);
        }
    }

    public synchronized void completed(){
        observers.forEach(observer->{
            observer.completed();
        });
    }

    @Override
    public synchronized Unsubscribtion subscribe(Observer<T> observer) {
        this.add(observer);
        observer.next(data);
        return new Unsubscribtion() {
            @Override
            public void unsubscribe() {
               Subject.this.remove(observer);
            }
        };
    }

    @Override
    public synchronized void remove(Observer<T> observer) {
        Iterator<Observer<T>> observerIterator=Subject.this.observers.iterator();
        while(observerIterator.hasNext()){
            Observer<T> currentObserver=observerIterator.next();
            if(currentObserver==observer){
                observerIterator.remove();
            }
        }
    }

    @Override
    public synchronized void add(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public String name() {
        return null;
    }

    public synchronized void updateDate(T data){
        this.data=data;
        this.infoAll();
    }
}
