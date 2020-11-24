package com.chongba.schedule.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:52
 */
@Slf4j
public class VisiableThreadPool extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        super.execute(task);
        logs("do execute。。。");
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Future<T> future = super.submit(task);
        logs("do submit");
        return future;
    }

    private void logs(String info) {
        //收集线程池运行状况数据信息
        //线程名称前缀
        String prefix = this.getThreadNamePrefix();
        //任务总数
        long taskCount = this.getThreadPoolExecutor().getTaskCount();
        //已完成的任务数
        long completedTaskCount = this.getThreadPoolExecutor().getCompletedTaskCount();
        //当前正在执行任务的线程数
        int activeCount = this.getThreadPoolExecutor().getActiveCount();
        //任务等待队列中任务数
        int queueSize = this.getThreadPoolExecutor().getQueue().size();
        log.info("{},{},taskCout={},completedTaskCount={},activeCount={},queueSize={}",
                prefix, info, taskCount, completedTaskCount, activeCount, queueSize);
    }
}
