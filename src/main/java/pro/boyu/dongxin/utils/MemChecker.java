package pro.boyu.dongxin.utils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.util.SafeEncoder;

import java.util.concurrent.*;

public class MemChecker implements Runnable {
    private MemChecker() {};

    private static final ShardedJedis jedis = SharedJedisTestFactory.getInstance().getJedis();

    public static void init() {
        MemChecker memChecker = new MemChecker();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(memChecker, 2000, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            String cur_key_name = KeyManager.getKey();
            if (cur_key_name == null) {
                return;
            }
            Long memBytes = (Long) jedis.sendCommand(() -> SafeEncoder.encode("memory"),
                    SafeEncoder.encode("usage"),
                    SafeEncoder.encode(cur_key_name));
            if (memBytes == null) {
                System.out.printf("Mem usage of %s cannot be obtained, because it is not exist in redis\n", cur_key_name);
                return;
            }
            long memKB = memBytes / 1000;
            long memMB = memKB / 1024;
            long memGB = memMB / 1024;
            System.out.printf("Mem usage of %s in kb: %d mb: %d GB: %d\n", cur_key_name, memKB, memMB, memGB);
        } catch (Exception e) {
            System.out.println("MEM CHECKER ERROR: " + e.getMessage());
        }
    }
}
