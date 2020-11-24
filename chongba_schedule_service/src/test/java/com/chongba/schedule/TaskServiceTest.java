package com.chongba.schedule;

import com.alibaba.fastjson.JSON;
import com.chongba.cache.CacheService;
import com.chongba.entity.Task;
import com.chongba.schedule.inf.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 14:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CacheService cacheService;

    @Test
    public void testPoolTask(){

        for (int i = 0; i < 10; i++) {
            Task task = new Task();

            task.setTaskType(250);
            task.setPriority(250);
            task.setExecuteTime(System.currentTimeMillis() + 5000 * i);
            task.setParameters("testpooltask".getBytes());

            taskService.addTask(task);
        }

        while (taskService.size(1,1) > 0){
            Task task = taskService.poll(1,1);

            if (task!=null){
                System.out.println("任务成功消费："+task.getTaskId());
            }


            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testSyncData(){
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            task.setTaskType(25);
            task.setPriority(250);
            task.setExecuteTime(System.currentTimeMillis() + 5000 * i);
            task.setParameters("testpooltask".getBytes());

            taskService.addTask(task);
        }
    }

    @Test
    public void testKeys(){
        Set<String> keys = cacheService.keys("future_*");
        System.out.println(keys);

        Set<String> scan = cacheService.scan("future_*");
        System.out.println(scan);
    }

    /** 
     * @description: 耗时：12513
     * @param: * @param:  
     * @return: void 
     * @author Mr-CHEN
     * @date: 2020-11-24 17:04
     */ 
    @Test
    public void testPiple1(){
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            Task task = new Task();

            task.setTaskType(1001);
            task.setPriority(1);
            task.setExecuteTime(System.currentTimeMillis() + 5000 * i);
            task.setParameters("test piple".getBytes());

            cacheService.lRightPush("1001_1", JSON.toJSONString(task));

        }
        System.out.println("耗时："+ (System.currentTimeMillis() - start));
    }

    /** 
     * @description: 耗时：358
     * @param: * @param:  
     * @return: void 
     * @author Mr-CHEN
     * @date: 2020-11-24 17:07
     */ 
    @Test
    public void testPiple2(){
        long start = System.currentTimeMillis();

        String key = "1002_2";

        Collection<String> tasks = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Task task = new Task();

            task.setTaskType(1001);
            task.setPriority(1);
            task.setExecuteTime(System.currentTimeMillis() + 5000 * i);
            task.setParameters("test piple".getBytes());
            tasks.add(JSON.toJSONString(task));

        }
        String[] allTask = tasks.toArray(new String[tasks.size()]);
        cacheService.getstringRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection)redisConnection;

                stringRedisConnection.lPush(key,allTask);
                return null;
            }
        });


        System.out.println("耗时："+ (System.currentTimeMillis() - start));
    }
}
