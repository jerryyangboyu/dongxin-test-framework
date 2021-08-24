package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Parameter;

import pro.boyu.dongxin.framework.annotations.CaseSubject;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.subscription.ExecutorSubject;
import pro.boyu.dongxin.utils.logger.TestCaseInfoUtil;

public class SyncExecutor extends Executor {
	private final Object lockObject;
	private final ExecutorSubject subject;

	public SyncExecutor(MethodExecutionInfo methodExecutionInfo, Object lock) {
		super(methodExecutionInfo);
		this.lockObject = lock;
		this.subject = new ExecutorSubject(methodExecutionInfo.getClassName(), methodExecutionInfo.getMethodName());
		this.injectCaseSubject();
	}
	
	private void injectCaseSubject() {
		Parameter[] parameters=this.method.getParameters();
		for(int index=0;index<parameters.length;index++) {
			if(parameters[index].isAnnotationPresent(CaseSubject.class)) {
				this.args[index]=this.subject;
			}
		}
 	}

	@Override
	public void run() {
		synchronized (lockObject) {
			int time = 0;
			try {
				this.subject.updateData(new ExecutionInfo() {
					{
						this.setMessage(method.getName());
						this.setState(TestCaseState.START);
						this.setTime(System.currentTimeMillis());
					}
				});

				for (; time < this.testMethod.repeatTime(); time++) {
					this.method.setAccessible(true);
					this.method.invoke(targetObject, args);
				}
				this.subject.completedSuccess();
				
			} catch (Exception e) {
				this.subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.RUNNING, TestCaseInfoUtil.executeExceptionInfoUtil(method.getName(), time, e.getMessage())));
				this.subject.completedError(e);
			}
			finally{
				this.lockObject.notify();
			}
		}

	}

	@Override
	public ExecutorSubject testMethodObservable() {
		return this.subject;
	}

	public long maxWait(){
		return this.testMethod.maxTime();
	}

	
}