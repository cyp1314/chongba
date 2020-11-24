package com.chongba.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chongba.schedule.pojo.TaskInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaskInfoMapper extends BaseMapper<TaskInfoEntity> {

    @Select(value = "select * from taskinfo where task_type=${taskType} and priority=${priority} and execute_time<=#{future}")
    public List<TaskInfoEntity> queryFutureTime(@Param("taskType") int type, @Param("priority") int priority, @Param("future") Long future);
}
