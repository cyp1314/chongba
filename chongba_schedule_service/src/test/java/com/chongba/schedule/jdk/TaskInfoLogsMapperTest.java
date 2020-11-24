package com.chongba.schedule.jdk;

import com.chongba.schedule.ScheduleApplication;
import com.chongba.schedule.mapper.TaskInfoLogsMapper;
import com.chongba.schedule.pojo.TaskInfoLogsEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 18:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class TaskInfoLogsMapperTest {

    @Autowired
    private TaskInfoLogsMapper taskInfoLogsMapper;


    @Test
    public void test(){
        TaskInfoLogsEntity taskInfoLogsEntity = new TaskInfoLogsEntity();

        taskInfoLogsEntity.setVersion(1);
        taskInfoLogsEntity.setStatus(1);
        taskInfoLogsEntity.setExecuteTime(new Date());
        taskInfoLogsEntity.setPriority(1);
        taskInfoLogsEntity.setTaskType(1);
        taskInfoLogsEntity.setParameters("log".getBytes());

        taskInfoLogsMapper.insert(taskInfoLogsEntity);

        System.out.println("插入后的主键："+taskInfoLogsEntity.getTaskId());

        taskInfoLogsEntity.setStatus(0);
        taskInfoLogsEntity.setVersion(1);
        taskInfoLogsMapper.updateById(taskInfoLogsEntity);

        taskInfoLogsEntity = taskInfoLogsMapper.selectById(taskInfoLogsEntity.getTaskId());
        System.out.println("第一次查询："+taskInfoLogsEntity.toString());

        taskInfoLogsEntity.setParameters("haha".getBytes());
        taskInfoLogsMapper.updateById(taskInfoLogsEntity);
        System.out.println("第二次查询："+taskInfoLogsEntity.toString());
    }

}
