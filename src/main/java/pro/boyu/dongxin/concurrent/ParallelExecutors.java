package pro.boyu.dongxin.concurrent;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.exception.OutTimeLimitException;
import pro.boyu.dongxin.framework.executor.Executor;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.framework.subscription.ExecutionObserver;
import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    @Author Yang Boyu
    @Date 2021.8.24
    @Description Embedded Concurrent Simulation
 */
public class ParallelExecutors extends Executor {
    public static CountDownLatch listenerInitial = new CountDownLatch(1);
    Logger logger = LoggerFactory.getLogger(ParallelExecutors.class);
    MethodExecutionInfo info;
    final Object lock;

    public ParallelExecutors(MethodExecutionInfo info, Object lock) {
        super(info);
        this.info = info;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            exec();
        }
    }

    public void exec() {

        TimeUnit timeUnit = info.getTimeUnit();
        int expireTime = info.getTestMethod().maxTime();
        int parallelism = info.getTestMethod().threadsNum();

        subject.updateData(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.START));

        JUCManager jucManager = new JUCManager();
        CountDownLatch listenerCountDownLatch = new CountDownLatch(1);

        for (int i = 0; i < parallelism; i++) {
            jucManager.register(new RepeatedThread(info));
        }

        new Listener(jucManager, info, subject, listenerCountDownLatch);

        //scheduled task to terminate all running process
        ScheduledExecutorService killerService = Executors.newSingleThreadScheduledExecutor();
        killerService.schedule(() -> {
            boolean error = false;
            for (RepeatedThread executor: jucManager.threads) {
                if (executor.isAlive()) {
                    error = true;
                    executor.interrupt();
                }
            }
            if (error) {
                subject.completedError(new OutTimeLimitException(expireTime));
            }
            listenerCountDownLatch.countDown();
            killerService.shutdown();
        }, expireTime, timeUnit);

        try {
            listenerCountDownLatch.await(); // wait for listener/killer thread to finish
            if (!killerService.isShutdown()) {
                // condition if listener successfully finished, but killer service still scheduled.
                killerService.shutdownNow();
            }
        } catch (Exception e) {
            logger.debug(e.getMessage() + e.getCause());
        } finally {
            // init listener initial again
            listenerInitial =  new CountDownLatch(1);
        }
    }
}
