package com.uusafe.platform.test.common.utils.jucservice;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchPublisher extends JedisTestJUCPublisher<CountDownLatch> {

	public CountDownLatchPublisher(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	
	public void reInitCountDownLatch(int num) {
		this.updateData(new CountDownLatch(num));
	}
}
