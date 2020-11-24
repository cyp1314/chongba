package com.chongba.schedule;

import com.alibaba.fastjson.JSON;
import com.chongba.cache.CacheService;
import com.chongba.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 13:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void test1(){
        cacheService.set("itcast1","itcast1");

        String itcast1 = cacheService.get("itcast1");
        System.out.println(itcast1);

        cacheService.delete("itcast1");
    }

    @Test
    public void test2(){
        cacheService.set("itcast2","itcast2");
        cacheService.expire("itcast2",10, TimeUnit.SECONDS);
    }

    @Test
    public void test3(){
        cacheService.setEx("itcast3","itcast3",10,TimeUnit.SECONDS);
    }

    @Test
    public void test4(){
        cacheService.lLeftPush("list","1");
        cacheService.lLeftPush("list","2");
        cacheService.lLeftPush("list","3");
        cacheService.lLeftPush("list","4");

        cacheService.lRightPush("list","1");
        cacheService.lRightPush("list","2");
        cacheService.lRightPush("list","3");
        cacheService.lRightPush("list","4");
    }

    @Test
    public void test5(){
        String list = cacheService.lLeftPop("list");
        System.out.println(list);
    }

    @Test
    public void test6(){
        cacheService.hPut("myhash","a","a");
        cacheService.hPut("myhash","b","b");
        cacheService.hPut("myhash","c","c");

        Map<String,String> map = new HashMap<>();
        map.put("d","d");
        map.put("e","e");
        map.put("f","f");
        cacheService.hPutAll("myhash",map);
    }

    @Test
    public void test7(){
        for (int i = 0; i < 100; i++) {
            Task task = new Task();

            task.setTaskType(1001);
            task.setPriority(50);
            task.setExecuteTime(System.currentTimeMillis());
            task.setParameters("cacheServiceTest".getBytes());

            cacheService.zAdd("task1", JSON.toJSONString(task),task.getExecuteTime());
        }

        cacheService.expire("task1",5,TimeUnit.SECONDS);

        Set<String> task1 = cacheService.zRange("task1", 0, 10);
        for (String task:task1) {
            System.out.println(task);
        }
        System.out.println("------------------------");

        Set<String> task11 = cacheService.zRange("task1", 0, -1);
        Set<String> task12 = cacheService.zRangeAll("task1");
        System.out.println(task11.size() == task12.size());
    }
}
