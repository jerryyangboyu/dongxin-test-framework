package pro.boyu.dongxin.framework.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import pro.boyu.dongxin.utils.Logger;
import pro.boyu.dongxin.utils.LoggerFactory;

import pro.boyu.dongxin.framework.OrderedPackageLoader;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.TestExecuteInfo;
import pro.boyu.dongxin.utils.iface.Observable;
import pro.boyu.dongxin.utils.iface.Observer;
import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;
import static pro.boyu.dongxin.framework.executor.TestCaseInfoUtil.getLocalDate;

public class TestMethodObserver implements Observer<TestExecuteInfo>{
	private static Logger logger=LoggerFactory.getLogger(TestMethodObserver.class);
	 TestExecuteInfo startInfo;
	
	 List<TestExecuteInfo> runningInfo=new LinkedList<TestExecuteInfo>();
	
	 TestExecuteInfo endInfo;
	 
	 Unsubscribtion unsubscribtion;
	 String methodName;
	 
	 public void subscribe(Observable<TestExecuteInfo> observable) {
		 this.unsubscribtion=observable.subscribe(this);
	 }
	 
	 public TestMethodObserver(String methodName) {
		 this.methodName=methodName;
	 }
	
	 TestCaseState state;
	@Override
	public void next(TestExecuteInfo data) {
		// TODO Auto-generated method stub
		switch (data.getState()) {
		case START:
			this.startInfo=data;
			this.runningInfo.add(data);
			break;
		case RUNNING:
			this.runningInfo.add(data);
			break;
		case ERROR:
			this.runningInfo.add(data);
			break;
		case SUCCESSFIN:
			this.runningInfo.add(data);
			this.endInfo=data;
		case ERRORFIN:
			this.runningInfo.add(data);
			this.endInfo=data;
		default:
			break;
		}
	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub
		this.runningInfo.add(new TestExecuteInfo(System.currentTimeMillis(), TestCaseState.RUNNING, e.getMessage()));
		
	}

	@Override
	public void completed() {
		// TODO Auto-generated method stub
		this.unsubscribtion.unsubscribe();
		
	}
	
	public void exportInfos() {
		synchronized (OrderedPackageLoader.loggerLockObject) {
			this.runningInfo.forEach(ri->{
				logger.info("testcase {} log in {} ,message : ",this.methodName,getLocalDate(ri.getTime()),ri.getMessage());
			});
		}
	}
	
	public void showInfo() {
		logger.info("testcase : {} fin , start at {} ,end at {} , state {}",methodName,getLocalDate(this.startInfo.getTime()),getLocalDate(this.endInfo.getTime()),this.state);
	}
	
	public List<TestExecuteInfo> showErrors() {
		return this.runningInfo.stream().filter((value)->{
			return value.getState()==TestCaseState.ERROR;
		}).collect(Collectors.toList());
	}

}
