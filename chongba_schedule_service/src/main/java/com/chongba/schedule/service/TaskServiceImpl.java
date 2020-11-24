package com.chongba.schedule.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ThreadPoolTaskExecutor myThreadPool;

    @Transactional
    @Override
    public long addTask(Task task) throws ScheduleSystemException {

        Future<Long> future = myThreadPool.submit(new Callable<Long>() {

            @Override
            public Long call() throws Exception {
                TaskInfoEntity taskInfoEntity = saveTask(task);
                return taskInfoEntity.getTaskId();
            }
        });
        long taskId = -1;
        try {
            taskId = future.get(5, TimeUnit.SECONDS);
        } catch (Exception e){
            throw new ScheduleSystemException(e);
        }
        return taskId;
    }

    private void addTaskToCache(TaskInfoEntity task) {

        String key = task.getTaskType() + "_" + task.getPriority();

        if (task.getExecuteTime() <= System.currentTimeMillis()){
            cacheService.lRightPush(Constants.TOPIC+key,JSON.toJSONString(task));
        }else {
            cacheService.zAdd(Constants.FUTURE+key, JSON.toJSONString(task),task.getExecuteTime());
        }
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
    public long size(int type,int priority) {

        String key = type + "_" + priority;

        Set<String> future_task = cacheService.zRangeAll(Constants.FUTURE + key);

        Long now_task = cacheService.lLen(Constants.TOPIC + key);

        return future_task.size() + now_task;
    }

    @Transactional
    @Override
    public Task poll(int type,int priority) throws TaskNotExistException {

        Future<TaskInfoEntity> future = myThreadPool.submit(new Callable<TaskInfoEntity>() {
            @Override
            public TaskInfoEntity call() throws Exception {
                TaskInfoEntity task = null;

                try {

                    String key = type + "_" + priority;

                    String task_json = cacheService.lLeftPop(Constants.TOPIC + key);

                    if (!StringUtils.isEmpty(task_json)){
                        task = JSON.parseObject(task_json,TaskInfoEntity.class);

                        cacheService.zRemove(Constants.DBCACHE,task_json);

                        updateDb(task.getTaskId(),Constants.EXECUTED);
                    }
                    return task;

                } catch (TaskNotExistException e){
                    log.warn("poll task exception");
                    throw  new TaskNotExistException(e);
                }
            }
        });

        Task result = null;

        try {
            TaskInfoEntity taskInfoEntity = future.get(5, TimeUnit.SECONDS);
            BeanUtils.copyProperties(taskInfoEntity,result);
            return result;
        } catch (Exception e){
            throw new TaskNotExistException(e);
        }

    }

    private void removeTaskFromCache(Task task) {

        String key = task.getTaskType()+"_" + task.getPriority();

        if (task.getExecuteTime() <= System.currentTimeMillis()){
            cacheService.lRemove(Constants.TOPIC+key,0,JSON.toJSONString(task));
        }else {
            cacheService.zRemove(Constants.FUTURE+key,JSON.toJSONString(task));
        }
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

    private TaskInfoEntity saveTask(Task task) throws ScheduleSystemException{
        TaskInfoEntity taskInfo = new TaskInfoEntity();
        try {
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

        } catch (ScheduleSystemException e){
            log.warn("add task exception taskid={}",task.getTaskId());
            throw new ScheduleSystemException(e.getMessage());
        }

        return taskInfo;
    }


    @PostConstruct
    private void syncData(){
        log.debug("------------------初始化---------------------");
        System.out.println("init............");

        clearCache();

        QueryWrapper<TaskInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.select("task_type","priority");
        wrapper.groupBy("task_type","priority");
        List<Map<String, Object>> maps = taskInfoMapper.selectMaps(wrapper);

        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(maps.size());

        for (Map<String, Object> map : maps) {


            myThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    String taskType = String.valueOf(map.get("task_type"));
                    String priority = String.valueOf(map.get("priority"));


                    QueryWrapper<TaskInfoEntity> subWrapper = new QueryWrapper<>();
                    subWrapper.eq("task_type",taskType);
                    subWrapper.eq("priority",priority);
                    List<TaskInfoEntity> taskInfoEntities = taskInfoMapper.selectList(subWrapper);
                    for (TaskInfoEntity taskInfoEntity : taskInfoEntities) {
                        addTaskToCache(taskInfoEntity);
                    }
                    latch.countDown();
                    log.info("线程{}，计数器{}，每组恢复耗时{}",Thread.currentThread().getName(),latch.getCount(),System.currentTimeMillis() - start);
                }
            });
        }
        try {
            latch.await(1,TimeUnit.MINUTES);
            log.info("数据恢复完成,总耗时：{}毫秒",(System.currentTimeMillis()-start));
        }catch (InterruptedException e){
            log.info("数据失败，{}",e.getMessage());
        }
    }

    private void clearCache() {
//        cacheService.delete(Constants.DBCACHE);

        Set<String> futureKeys = cacheService.scan(Constants.FUTURE + "*");
        cacheService.delete(futureKeys);

        Set<String> topicKeys = cacheService.scan(Constants.TOPIC + "*");
        cacheService.delete(topicKeys);

    }
}
