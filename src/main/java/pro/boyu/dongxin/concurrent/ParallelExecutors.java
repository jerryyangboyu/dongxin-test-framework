package pro.boyu.dongxin.concurrent;

import pro.boyu.dongxin.framework.exception.OutTimeLimitException;
import pro.boyu.dongxin.framework.executor.Executor;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
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
    private static final Logger logger = LoggerFactory.getLogger(ParallelExecutors.class);
    MethodExecutionInfo info;

    public ParallelExecutors(MethodExecutionInfo info) {
        super(info);
        this.info = info;
    }

    @Override
    public void run() {
        exec();
    }

    public void exec() {
        TimeUnit timeUnit = info.getTimeUnit();
        int expireTime = info.getTestMethod().maxTime();
        int parallelism = info.getTestMethod().threadsNum();

        JUCManager jucManager = new JUCManager();
        for (int i = 0; i < parallelism; i++) {
            jucManager.register(new RepeatedThread(info));
        }
        new Listener(jucManager, info);

        // scheduled task to terminate all running process
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
            } else {
                subject.completedSuccess();
            }
            killerService.shutdown();
        }, expireTime, timeUnit);

        // init listener initial again
        listenerInitial =  new CountDownLatch(1);
    }
}
