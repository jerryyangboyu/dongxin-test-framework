package com.uusafe.platform.test.common.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class JUCManager {
    CountDownLatch countDownLatch;
    CyclicBarrier cyclicBarrier;
    List<Subscriber> threads = new LinkedList<Subscriber>();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public JUCManager() {
    }

    public void register(Subscriber sub) {
        this.threads.add(sub);
        try {
            ((Thread) sub).start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public synchronized CountDownLatch reInitCountDownLatch(final int count) {
        this.countDownLatch = new CountDownLatch(count);
        for (Subscriber sub : threads) {
            sub.refreshCountDownLath(this.countDownLatch);
        }
        return this.countDownLatch;
    }

    public synchronized CyclicBarrier reInitCyclicBarrier(final int count) {
        this.cyclicBarrier = new CyclicBarrier(count);
        for (Subscriber sub : threads) {
            sub.refreshCyclicBarrier(cyclicBarrier);
        }
        return this.cyclicBarrier;
    }

}
