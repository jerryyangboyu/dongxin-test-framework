package pro.boyu.dongxin.concurrent;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.executor.Executor;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class RepeatedThread extends Executor implements Subscriber {
    Logger logger = LoggerFactory.getLogger(testClass);
    CountDownLatch countDownLatch = null;
    CyclicBarrier cyclicBarrier = null;

    public RepeatedThread(MethodExecutionInfo info) {
        super(info);
    }

    @Override
    public void run() {
        this.subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.START));
        try {
            logger.debug("Launching thread");
            ParallelExecutors.listenerInitial.await(); // wait for listener thread
            this.cyclicBarrier.await(); // after main thread send init command, all thread start to init and invoke

            method.setAccessible(true);
            for (int i = 0; i < testMethod.repeatTime(); i++) {
                if (interrupted()) break;
                method.invoke(targetObject, args);
            }

            this.countDownLatch.countDown(); // after finish execution, decrement the count to mark the thread has finished
        } catch (InterruptedException | BrokenBarrierException | InvocationTargetException | IllegalAccessException e) {
            this.subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.ERROR, e.getMessage()));
        }
    }

    @Override
    public void refreshCountDownLath(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void refreshCyclicBarrier(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }
}
