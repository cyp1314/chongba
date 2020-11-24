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
    
    /** 
     * @description: 获取任务数量
     * @param: * @param: type
     * @param: priority
     * @return: long 
     * @author Mr-CHEN
     * @date: 2020-11-24 16:10
     */ 
    public long size(int type,int priority);


    /**
     * @description: 拉取任务
     * @param: * @param: type
     * @param: priority
     * @return: com.chongba.entity.Task
     * @author Mr-CHEN
     * @date: 2020-11-24 16:11
     */
    public Task poll(int type,int priority) throws TaskNotExistException;
}
