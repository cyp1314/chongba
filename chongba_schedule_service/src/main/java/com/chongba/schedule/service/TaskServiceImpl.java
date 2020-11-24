package com.chongba.schedule.service;

import com.chongba.cache.CacheService;
import com.chongba.entity.Constants;
import com.chongba.entity.Task;
import com.chongba.exception.ScheduleSystemException;
import com.chongba.exception.TaskNotExistException;
import com.chongba.schedule.inf.TaskService;
import com.chongba.schedule.mapper.TaskInfoLogsMapper;
import com.chongba.schedule.mapper.TaskInfoMapper;
import com.chongba.schedule.pojo.TaskInfoEntity;
import com.chongba.schedule.pojo.TaskInfoLogsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 19:23
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskInfoLogsMapper taskInfoLogsMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public long addTask(Task task) throws ScheduleSystemException {

        try {
            TaskInfoEntity taskInfo = new TaskInfoEntity();

            taskInfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskInfo.setPriority(task.getPriority());
            taskInfo.setTaskType(task.getTaskType());
            taskInfo.setParameters(task.getParameters());

            taskInfoMapper.insert(taskInfo);

            taskInfo.setTaskId(taskInfo.getTaskId());

            TaskInfoLogsEntity taskLog = new TaskInfoLogsEntity();
            taskLog.setVersion(1);
            taskLog.setStatus(Constants.SCHEDULED);
            taskLog.setTaskId(taskInfo.getTaskId());
            taskLog.setExecuteTime(taskInfo.getExecuteTime());
            taskLog.setPriority(taskInfo.getPriority());
            taskLog.setTaskType(taskInfo.getTaskType());
            taskLog.setParameters(taskInfo.getParameters());

            taskInfoLogsMapper.insert(taskLog);

        } catch (ScheduleSystemException e){
            log.warn("add task exception taskid={}",task.getTaskId());
            throw new ScheduleSystemException(e.getMessage());
        }
        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(long taskId) throws TaskNotExistException {
        boolean flags = false;
        try {
            taskInfoMapper.deleteById(taskId);

            TaskInfoLogsEntity taskLog = taskInfoLogsMapper.selectById(taskId);
            taskLog.setStatus(Constants.CANCELLED);
            taskInfoLogsMapper.updateById(taskLog);

            flags = true;
        } catch (Exception e){
            log.warn("task cancel exception taskid={}",taskId);
            throw new TaskNotExistException(e);
        }
        return flags;
    }
}
