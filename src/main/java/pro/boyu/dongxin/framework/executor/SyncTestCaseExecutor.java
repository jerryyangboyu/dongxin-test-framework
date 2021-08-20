package pro.boyu.dongxin.framework.executor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import pro.boyu.dongxin.framework.annotations.CaseSubject;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import pro.boyu.dongxin.framework.infobean.TestExecuteInfo;
import pro.boyu.dongxin.framework.infobean.TestMethodInvokeInfo;
import pro.boyu.dongxin.utils.Subject;
import pro.boyu.dongxin.utils.iface.Observable;
import pro.boyu.dongxin.framework.constenum.TestCaseState;

public class SyncTestCaseExecutor extends TestMethodExecutor {
	Method method;
	Object targetObject;
	Object[] args;
	TestMethod testMethod;
	private Object lockObject;
	SyncTestCaseExecutorSubject syncTestCaseExecutorSubject;
	class SyncTestCaseExecutorSubject extends Subject<TestExecuteInfo>{

		public SyncTestCaseExecutorSubject() {
			super(SyncTestCaseExecutor.this.method.getName());
			// TODO Auto-generated constructor stub
		}
		
		
		protected synchronized void completedError(Exception e) {
			// TODO Auto-generated method stub
			this.getObservers().forEach((oberver)->{
				oberver.next(new TestExecuteInfo(System.currentTimeMillis(), TestCaseState.ERRORFIN, e.getMessage()));
				//oberver.completed();
			});
		}
		
		protected synchronized void completedSuccess() {
			this.getObservers().forEach(o->{
				o.next(new TestExecuteInfo(System.currentTimeMillis(), TestCaseState.SUCCESSFIN, null));
				//o.completed();
			});
		}
		
	}

	public SyncTestCaseExecutor(TestMethodInvokeInfo testMethodInvokeInfo, Object lock) {
		super(testMethodInvokeInfo);
		this.lockObject = lock;
		this.syncTestCaseExecutorSubject=new SyncTestCaseExecutorSubject();
		this.injectCaseSubject();
	}
	
	private void injectCaseSubject() {
		Parameter[] parameters=this.method.getParameters();
		for(int index=0;index<parameters.length;index++) {
			if(parameters[index].isAnnotationPresent(CaseSubject.class)) {
				this.args[index]=this.syncTestCaseExecutorSubject;
			}
		}
 	}

	@Override
	public void run() {
		synchronized (lockObject) {
			int time = 0;
			try {
				this.new SyncTestCaseExecutorSubject().updateData(new TestExecuteInfo() {
					{
						this.setMessage(method.getName());
						this.setState(TestCaseState.START);
						this.setTime(System.currentTimeMillis());
					}
				});

				for (; time < this.testMethod.repeatTime(); time++) {
					this.method.invoke(targetObject, args);
				}
				this.syncTestCaseExecutorSubject.completedSuccess();
				
			} catch (Exception e) {
				// TODO: handle exception
				this.syncTestCaseExecutorSubject.updateData(new TestExecuteInfo(System.currentTimeMillis(), TestCaseState.RUNNING, TestCaseInfoUtil.executeExceptionInfoUtil(method.getName(), time, e.getMessage())));
				this.syncTestCaseExecutorSubject.completedError(e);
			}
			
			
		}

	}

	@Override
	public Observable<TestExecuteInfo> testMethodObervable() {
		// TODO Auto-generated method stub
		return this.syncTestCaseExecutorSubject;
	}

	
}