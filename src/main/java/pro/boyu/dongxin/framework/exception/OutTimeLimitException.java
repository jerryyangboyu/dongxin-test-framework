package pro.boyu.dongxin.framework.exception;

/**
 * 一个Test Case执行超过设定最长执行时间会抛出该异常 在TestMethod上注明默认为60000ms
 */
public class OutTimeLimitException extends Exception{
	public OutTimeLimitException(long maxWaitTime) {
		// TODO Auto-generated constructor stub
		super("the case hasn't fin test case in "+maxWaitTime+" ms. ");
	}
}
