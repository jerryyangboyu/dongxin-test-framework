package pro.boyu.dongxin.utils.jucservice;


public interface Subscriber<T> {
	
	void process(T t);
	
	void setUnsubscribtion(Unsubscribtion unsubscribtion);
	
	default void completed() {
		this.unsubscribe();
	}
	
	default void error(Exception e) {
		
	}
	
	void unsubscribe();
	
	String id();

}
