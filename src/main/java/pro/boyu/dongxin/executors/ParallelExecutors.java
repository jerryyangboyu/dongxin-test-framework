package pro.boyu.dongxin.executors;

import pro.boyu.dongxin.framework.AbstractPackageLoader;
import pro.boyu.dongxin.utils.Logger;
import pro.boyu.dongxin.utils.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    @Author Zhang Ying
    @Date 2021.8.8
    @Description An implementation of concurrent simulation util
    @Example
    SharedJedisTestFactory factory = SharedJedisTestFactory.getInstance();
    ParallelExecutors.exec(() -> {
        ShardedJedis jedis = factory.getJedis();
        System.out.println(jedis.get("Hello"));
        jedis.close();
    }, 10);
 */
public class ParallelExecutors {
    public static CountDownLatch listenerInitial = new CountDownLatch(1);
    public static int parallelism;
    private static final Logger logger = LoggerFactory.getLogger(ParallelExecutors.class);

    public static void exec(Runnable runnable, int parallelism, int expireTime, TimeUnit timeUnit) {
        String lastSubscriberId = AbstractPackageLoader.publisher.getLastSubscriberName();
        AbstractPackageLoader.publisher.setSubscriberPendingStatus(lastSubscriberId, true);

        JUCManager jucManager = new JUCManager();
        for (int i = 0; i < parallelism; i++) {
            jucManager.register(new RunnableExecutor(i, runnable));
        }
        new Listener(jucManager);

//         scheduled task to terminate all running process
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            boolean error = false;
            for (Subscriber subscriber: jucManager.threads) {
                Thread t = (Thread) subscriber;
                if (t.isAlive()) {
                    // TODO register subscriber on execution handler.
                    error = true;
                    logger.debug("Stopping " + t.getName());
                    t.stop(); // TODO a more elegant way is needed
                }
            }
            service.shutdown();
            if (error) {
                AbstractPackageLoader.publisher.invoke(lastSubscriberId, false, "ERROR: TIME OUT");
            } else {
                AbstractPackageLoader.publisher.invoke(lastSubscriberId, true, null);
            }
        }, expireTime, timeUnit);

        // init listener initial again
        listenerInitial =  new CountDownLatch(1);
    }

    public static void exec(Runnable runnable)  {
        exec(runnable, 8, 3, TimeUnit.SECONDS);
    }

    public static void exec(Runnable runnable, int parallelism) {
        exec(runnable, parallelism, 3, TimeUnit.SECONDS);
    }
}
