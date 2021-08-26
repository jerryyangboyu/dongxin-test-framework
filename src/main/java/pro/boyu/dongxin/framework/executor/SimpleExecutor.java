package pro.boyu.dongxin.framework.executor;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.utils.logger.TestCaseInfoUtil;

public class SimpleExecutor extends Executor {
	private final Object lockObject;

	public SimpleExecutor(MethodExecutionInfo info, Object lock) {
		super(info);
		this.lockObject = lock;
	}

	@Override
	public void run() {
		synchronized (lockObject) {
			int time = 0;
			try {
				this.subject.updateData(new ExecutionInfo() {
					{
						this.setMessage(subject.getMethodName());
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
				this.subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.RUNNING, TestCaseInfoUtil.executeExceptionInfoUtil(testClass.getName(), method.getName(), e.getMessage())));
				this.subject.completedError(e);
			}
			finally{
				this.lockObject.notify();
			}
		}

	}
}