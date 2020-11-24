package com.chongba.schedule.cron;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:46
 */
@Component
public class AsyncTask {

    @Async(value = "myThreadPool")
    public void myAsyncTask(){
        System.out.println("async task " + Thread.currentThread().getName());
    }
}
