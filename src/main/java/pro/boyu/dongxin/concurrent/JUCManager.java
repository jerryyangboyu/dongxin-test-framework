package pro.boyu.dongxin.concurrent;

import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class JUCManager {
    CountDownLatch countDownLatch;
    CyclicBarrier cyclicBarrier;
    List<RepeatedThread> threads = new LinkedList<>();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public JUCManager() {
    }

    public void register(RepeatedThread executor) {
        this.threads.add(executor);
        try {
            executor.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public synchronized void reInitCountDownLatch(final int count) {
        this.countDownLatch = new CountDownLatch(count);
        for (Subscriber sub : threads) {
            sub.refreshCountDownLath(this.countDownLatch);
        }
    }

    public synchronized void reInitCyclicBarrier(final int count) {
        this.cyclicBarrier = new CyclicBarrier(count);
        for (Subscriber sub : threads) {
            sub.refreshCyclicBarrier(cyclicBarrier);
        }
    }

}
