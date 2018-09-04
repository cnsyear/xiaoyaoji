package cn.com.xiaoyaoji.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujingjie
 *         created on 2017/8/29
 */
public class ScheduleManager {
    private static ScheduledExecutorService scheduledExecutorService;
    private static Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    static {
        scheduledExecutorService = Executors.newScheduledThreadPool(2);

    }

    public static void schedule(Runnable runnable, Date first, long period) {
        schedule(runnable,first.getTime()-System.currentTimeMillis(),period);
    }
    public static void schedule(Runnable runnable, long delay, long period) {
        scheduledExecutorService.scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
    }


    public static void shutdown() {
        scheduledExecutorService.shutdownNow();
    }


}
