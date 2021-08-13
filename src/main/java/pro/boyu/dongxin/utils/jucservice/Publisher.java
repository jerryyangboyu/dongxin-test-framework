package com.uusafe.platform.test.common.utils.jucservice;

import com.uusafe.platform.test.common.utils.jucservice.AbstractPublisher.InvalidSubscriberIdException;

public interface Publisher<T> {
	public void rigster(Subscriber<T> subscriber) throws InvalidSubscriberIdException;
	
	public void updateData(T t);
	
	public void remove(String id) throws InvalidSubscriberIdException;
	
	public void infoAll();
	
	
	public String publisherID();
	
}
