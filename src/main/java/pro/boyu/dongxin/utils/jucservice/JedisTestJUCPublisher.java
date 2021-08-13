package com.uusafe.platform.test.common.utils.jucservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JedisTestJUCPublisher<T> extends AbstractPublisher<T> {
	private static final Logger logger=LoggerFactory.getLogger("");
	public JedisTestJUCPublisher(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	private final Map<String,Subscriber<T>> subscriberMap=new HashMap<String,Subscriber<T>>();

	@Override
	public synchronized void rigster(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		if(this.subscriberMap.values().contains(subscriber)) {
			logger.warn("subscriber {} has already register publisher {} ",subscriber.id(),publisherID());
			subscriber.process(data);
			return ;
		}
		this.appendSubscriber(subscriber);
		subscriber.setUnsubscribtion(()->{
			this.remove(subscriber.id());
		});
		subscriber.process(data);
	}
	
	private synchronized void appendSubscriber(Subscriber<T> subscriber) {
		this.subscriberMap.put(subscriber.id(), subscriber);
	}

	@Override
	public synchronized void remove(String id) {
		// TODO Auto-generated method stub
		this.subscriberMap.remove(id);
	}

	@Override
	public synchronized void infoAll() {
		// TODO Auto-generated method stub
		for(Subscriber<T> subscriber:this.subscriberMap.values()) {
			subscriber.process(data);
		}
		
	}

}
