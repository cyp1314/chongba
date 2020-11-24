package com.chongba.schedule;

import com.chongba.entity.Task;
import com.chongba.schedule.inf.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

        while (taskService.size() > 0){
            Task task = taskService.poll();

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
}
