package pro.boyu.dongxin.concurrent;

import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.framework.subscription.ExecutorSubject;
import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class Listener extends RepeatedThread implements Subscriber {
    private final JUCManager jucManager;
    private final Logger logger = LoggerFactory.getLogger("Control Thread");
    private final int threadNum;
    CountDownLatch countDownLatch;
    CountDownLatch listenerCountDownLatch;
    CyclicBarrier cyclicBarrier;

    public Listener(JUCManager jucManager, MethodExecutionInfo info, ExecutorSubject subject, CountDownLatch latch) {
        super(info);
        this.jucManager = jucManager;
        this.jucManager.register(this);
        this.threadNum = info.getTestMethod().threadsNum();
        this.subject = subject;
        listenerCountDownLatch = latch;
    }

    // 控制整个测试流程
    @Override
    public void run() {
        logger.debug("Control Thread has started");
        jucManager.reInitCountDownLatch(threadNum);
        jucManager.reInitCyclicBarrier(threadNum + 1);
        logger.debug("Control Thread Has finished init.");
        ParallelExecutors.listenerInitial.countDown(); // finish init, start exec all others threads
        try {
            this.cyclicBarrier.await();  // wait for all threads to init and exec
            this.countDownLatch.await(); // wait for all threads to finish exec
            logger.debug("Thread has been finished");
            subject.completedSuccess();
            listenerCountDownLatch.countDown(); // mark listener thread finish
        } catch (InterruptedException | BrokenBarrierException e1) {
            logger.debug(e1.getMessage() + e1.getCause());
        }

    }

    @Override
    public void refreshCountDownLath(CountDownLatch countDownLatch) {
        synchronized (this) {
            this.countDownLatch = countDownLatch;
        }
    }

    @Override
    public void refreshCyclicBarrier(CyclicBarrier cyclicBarrier) {
        synchronized (this) {
            this.cyclicBarrier = cyclicBarrier;
        }
    }
}
