package pro.boyu.dongxin.executors;

public class OrderExecutors {
    public static int parallelism;

    public static void exec(Runnable runnable, int parallelism) {
        for (int i = 0; i < parallelism; i++) {
            new Thread(runnable).start();
        }
    }
}
