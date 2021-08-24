package pro.boyu.dongxin.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public interface Subscriber {

    public void refreshCountDownLath(CountDownLatch countDownLatch);

    public void refreshCyclicBarrier(CyclicBarrier cyclicBarrier) ;
}
