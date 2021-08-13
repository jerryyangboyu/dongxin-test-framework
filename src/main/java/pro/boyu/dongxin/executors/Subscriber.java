package pro.boyu.dongxin.executors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public interface Subscriber {
    void refreshCountDownLath(CountDownLatch countDownLatch);

    void refreshCyclicBarrier(CyclicBarrier cyclicBarrier);
}
