package pro.boyu.dongxin.executors;

import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class RunnableExecutor extends Thread implements Subscriber {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private CountDownLatch countDownLatch;
    private CyclicBarrier cyclicBarrier;
    private final int threadID;
    private final Runnable runnable;

    public RunnableExecutor(int threadNum, Runnable runnable) {
        this.threadID = threadNum;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            logger.debug("Launching thread with thread number " + this.threadID);

            ParallelExecutors.listenerInitial.await(); // wait for listener thread
            this.cyclicBarrier.await(); // after main thread send init command, all thread start to init and invoke

            runnable.run();

            this.countDownLatch.countDown(); // after finish execution, decrement the count to mark the thread has finished
        } catch (InterruptedException | BrokenBarrierException e) {
            logger.error(e.getMessage());
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
