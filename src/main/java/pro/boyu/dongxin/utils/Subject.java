package pro.boyu.dongxin.utils;

import pro.boyu.dongxin.utils.iface.Observable;
import pro.boyu.dongxin.utils.iface.Observer;
import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Subject<T> implements Observable<T> {
	private String idString;
	public Subject(String id) {
		this.idString=id;
	}
	public String getId() {
		return this.idString;
	}
    private List<Observer<T>> observers=new LinkedList<Observer<T>>();
    public synchronized void updateData(T data){
        this.data=data;
        this.infoAll();
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

  
    
    
    protected synchronized void completedSuccess() {
    	
    }
    protected synchronized void completedError() {
    	
    }
    protected synchronized  List<Observer<T>> getObservers() {
		return observers;
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

   
    protected synchronized void remove(Observer<T> observer) {
        Iterator<Observer<T>> observerIterator=Subject.this.observers.iterator();
        while(observerIterator.hasNext()){
            Observer<T> currentObserver=observerIterator.next();
            if(currentObserver==observer){
                observerIterator.remove();
            }
        }
    }

    
    protected synchronized void add(Observer<T> observer) {
        this.observers.add(observer);
    }

    @Override
    public String name() {
        return idString;
    }

   
}
