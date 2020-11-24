package com.chongba.schedule.executor;

import com.chongba.schedule.ScheduleApplication;
import com.chongba.schedule.cron.AsyncTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:48
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class TestAsync {

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void test(){
        asyncTask.myAsyncTask();
    }
}
