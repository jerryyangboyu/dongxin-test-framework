package pro.boyu.dongxin.utils.jucservice;

import pro.boyu.dongxin.utils.jucservice.AbstractPublisher.InvalidSubscriberIdException;

public interface Publisher<T> {
	public void rigster(Subscriber<T> subscriber) throws InvalidSubscriberIdException;
	
	public void updateData(T t);
	
	public void remove(String id) throws InvalidSubscriberIdException;
	
	public void infoAll();
	
	
	public String publisherID();
	
}
