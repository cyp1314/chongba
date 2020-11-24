package com.chongba.schedule;

import lombok.Data;

import java.util.Calendar;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 17:23
 */
@Data
public class DelayedTask implements Delayed {

    // 任务的执行时间
    private int executeTime = 0;

    private String taskName;

    public DelayedTask(int dalay,String taskName) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, dalay);
        this.executeTime = (int) (calendar.getTimeInMillis() / 1000);
        this.taskName = taskName;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        Calendar calendar = Calendar.getInstance();

        return executeTime - (calendar.getTimeInMillis() / 1000);
    }

    @Override
    public int compareTo(Delayed o) {
        long val = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return val == 0 ? 0 : (val < 0 ? -1 : 1);
    }
}
