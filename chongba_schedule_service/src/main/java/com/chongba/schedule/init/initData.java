package com.chongba.schedule.init;

import com.chongba.cache.CacheService;
import com.chongba.entity.Constants;
import com.chongba.entity.Task;
import com.chongba.schedule.mapper.TaskInfoMapper;
import com.chongba.schedule.pojo.TaskInfoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 15:52
 */
@Component
public class initData {

    @Autowired
    private TaskInfoMapper taskMapper;

    @Autowired
    private CacheService cacheService;


}
