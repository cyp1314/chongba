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

    void addTask(Task task) throws ScheduleSystemException;

    boolean cancelTask(long taskId) throws TaskNotExistException;

    /**
     * @description: 获取任务数量
     * @param: * @param:
     * @return: long
     * @author Mr-CHEN
     * @date: 2020-11-24 14:14
     */
    public long size();

    /** 
     * @description: 拉取任务
     * @param: * @param:  
     * @return: com.chongba.entity.Task 
     * @author Mr-CHEN
     * @date: 2020-11-24 14:21
     */ 
    public Task poll() throws TaskNotExistException;
}
