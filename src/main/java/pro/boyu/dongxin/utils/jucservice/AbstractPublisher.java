package com.uusafe.platform.test.common.utils.jucservice;


public abstract class AbstractPublisher<T> implements Publisher<T>  {
	static class InvalidSubscriberIdException extends Exception{

		/**
		 * @author shadoowz
		 * @data 2021年8月8日
		 * @field serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;
		
		
		public InvalidSubscriberIdException(String message) {
			super(message);
		}
	    
	}
	private final String  publisherID;
	
	protected T data;
	
	public AbstractPublisher(String id) {
		this.publisherID=id;
	}

	@Override
	public synchronized void updateData(T data) {
		// TODO Auto-generated method stub
		this.data=data;
		infoAll();
		
	}
	
	@Override
	public String publisherID() {
		// TODO Auto-generated method stub
		return this.publisherID;
	}
	
	
	

}
