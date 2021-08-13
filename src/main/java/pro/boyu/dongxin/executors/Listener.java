package com.uusafe.platform.test.common.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class Listener extends Thread implements Subscriber {
    private final JUCManager jucManager;
    Logger logger = LoggerFactory.getLogger("Control Thread");
    CountDownLatch countDownLatch;
    CyclicBarrier cyclicBarrier;

    public Listener(JUCManager jucManager) {
        this.jucManager = jucManager;
        this.jucManager.register(this);
    }

    // 控制整个测试流程
    @Override
    public void run() {
        logger.debug("Control Thread has started");
        jucManager.reInitCountDownLatch(ParallelExecutors.parallelism);
        jucManager.reInitCyclicBarrier(ParallelExecutors.parallelism + 1);
        logger.debug("Control Thread Has finished init.");
        ParallelExecutors.listenerInitial.countDown(); // finish init, start exec all others threads
        try {
            this.cyclicBarrier.await();  // wait for all threads to init and exec
            this.countDownLatch.await(); // wait for all threads to finish exec
            logger.debug("Thread has been finished");
        } catch (InterruptedException | BrokenBarrierException e1) {
            logger.error(e1.getMessage());
        }

    }

    @Override
    public void refreshCountDownLath(CountDownLatch countDownLatch) {
        // TODO Auto-generated method stub
        synchronized (this) {
            this.countDownLatch = countDownLatch;
        }

    }

    @Override
    public void refreshCyclicBarrier(CyclicBarrier cyclicBarrier) {
        // TODO Auto-generated method stub
        synchronized (this) {
            this.cyclicBarrier = cyclicBarrier;

        }
    }
}
