package com.chongba.schedule.service;

import com.alibaba.fastjson.JSON;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

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

    @Transactional
    @Override
    public void addTask(Task task) throws ScheduleSystemException {

        addTaskToDb(task);
    }

    private void addTaskToCache(TaskInfoEntity task) {
        cacheService.zAdd(Constants.DBCACHE, JSON.toJSONString(task),task.getExecuteTime());
    }

    @Transactional
    @Override
    public boolean cancelTask(long taskId) throws TaskNotExistException {
        boolean flag = false;
        Task task = updateDb(taskId,Constants.CANCELLED);
        if (task!=null){
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }


    @Override
    public long size() {
        Set<String> tasks = cacheService.zRangeAll(Constants.DBCACHE);
        return tasks.size();
    }

    @Transactional
    @Override
    public Task poll() throws TaskNotExistException {

        TaskInfoEntity task = null;
        Task result = new Task();
        try {

            Set<String> tasks = cacheService.zRange(Constants.DBCACHE, 0, System.currentTimeMillis());

            if (tasks!=null && !tasks.isEmpty()){
                String task_json = tasks.iterator().next();

                if (!StringUtils.isEmpty(task_json)){
                    task = JSON.parseObject(task_json,TaskInfoEntity.class);

                    cacheService.zRemove(Constants.DBCACHE,task_json);

                    updateDb(task.getTaskId(),Constants.EXECUTED);
                }
            }

        } catch (TaskNotExistException e){
            log.warn("poll task exception");
            throw  new TaskNotExistException(e);
        }
        BeanUtils.copyProperties(task,result);
        return result;
    }

    private void removeTaskFromCache(Task task) {
        cacheService.zRemove(Constants.DBCACHE,JSON.toJSONString(task));
    }

    private Task updateDb(long taskId, int cancelled) {
        Task task = null;
        try {
            taskInfoMapper.deleteById(taskId);

            TaskInfoLogsEntity taskLog = taskInfoLogsMapper.selectById(taskId);
            taskLog.setStatus(Constants.CANCELLED);
            taskInfoLogsMapper.updateById(taskLog);

            task = new Task();
            BeanUtils.copyProperties(taskLog,task);
            task.setExecuteTime(taskLog.getExecuteTime());

        } catch (Exception e){
            log.warn("task cancel exception taskid={}",taskId);
            throw new TaskNotExistException(e);
        }
        return task;
    }

    private boolean addTaskToDb(Task task) throws ScheduleSystemException{
        boolean flag = false;

        try {
            TaskInfoEntity taskInfo = new TaskInfoEntity();

            taskInfo.setExecuteTime(task.getExecuteTime());
            taskInfo.setPriority(task.getPriority());
            taskInfo.setTaskType(task.getTaskType());
            taskInfo.setParameters(task.getParameters());

            int res = taskInfoMapper.insert(taskInfo);

            if (res>0){
                addTaskToCache(taskInfo);
            }




            taskInfo.setTaskId(taskInfo.getTaskId());

//            int i = 1/10;

            TaskInfoLogsEntity taskLog = new TaskInfoLogsEntity();
            taskLog.setVersion(1);
            taskLog.setStatus(Constants.SCHEDULED);
            taskLog.setTaskId(taskInfo.getTaskId());
            taskLog.setExecuteTime(taskInfo.getExecuteTime());
            taskLog.setPriority(taskInfo.getPriority());
            taskLog.setTaskType(taskInfo.getTaskType());
            taskLog.setParameters(taskInfo.getParameters());

            taskInfoLogsMapper.insert(taskLog);

            flag = true;
        } catch (ScheduleSystemException e){
            log.warn("add task exception taskid={}",task.getTaskId());
            throw new ScheduleSystemException(e.getMessage());
        }

        return flag;
    }


    @PostConstruct
    private void syncData(){
        log.debug("------------------初始化---------------------");
        System.out.println("init............");

        clearCache();

        List<TaskInfoEntity> allTasks = taskInfoMapper.selectList(null);

        for (TaskInfoEntity taskInfoEntity : allTasks) {
            addTaskToCache(taskInfoEntity);
        }
    }

    private void clearCache() {
        cacheService.delete(Constants.DBCACHE);
    }
}
