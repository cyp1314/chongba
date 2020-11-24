package com.chongba.schedule.inf;

import com.chongba.entity.Task;
import com.chongba.exception.ScheduleSystemException;
import com.chongba.exception.TaskNotExistException;

/**
 * @description: 对外访问接口
 * @param: * @param: null 
 * @return:  
 * @author Mr-CHEN
 * @date: 2020-11-23 19:22
 */ 
public interface TaskService {

    long addTask(Task task) throws ScheduleSystemException;

    boolean cancelTask(long taskId) throws TaskNotExistException;
}
