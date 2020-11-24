package com.chongba.schedule;

import com.chongba.schedule.mapper.TaskInfoMapper;
import com.chongba.schedule.pojo.TaskInfoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 18:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class TaskInfoMapperTest {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Test
    public void test1(){
        TaskInfoEntity taskInfoEntity = new TaskInfoEntity();
        taskInfoEntity.setExecuteTime(System.currentTimeMillis());
        taskInfoEntity.setPriority(1);
        taskInfoEntity.setTaskType(1001);
        taskInfoEntity.setParameters("test".getBytes());

        taskInfoMapper.insert(taskInfoEntity);

        System.out.println("返回的主键："+taskInfoEntity.getTaskId());


    }

}
