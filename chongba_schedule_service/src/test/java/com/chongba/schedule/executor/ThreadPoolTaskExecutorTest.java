package com.chongba.schedule.executor;

import com.chongba.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:39
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class ThreadPoolTaskExecutorTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testExec(){
        for (int i = 0; i < 100; i++) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadPoolTaskExecutor test "+Thread.currentThread().getName());
                }
            });
        }

        // ThreadPoolTaskExecutor支持对线程池参数的ioc配置
        System.out.println("核心线程数:"+threadPoolTaskExecutor.getCorePoolSize());
        System.out.println("最大线程数:"+threadPoolTaskExecutor.getMaxPoolSize());
        System.out.println("线程等待超时时间:"+threadPoolTaskExecutor.getKeepAliveSeconds());
        System.out.println("当前活跃的线程数:"+threadPoolTaskExecutor.getActiveCount());
        System.out.println("线程池内线程的名称前缀:"+threadPoolTaskExecutor.getThreadNamePrefix());
    }
}
