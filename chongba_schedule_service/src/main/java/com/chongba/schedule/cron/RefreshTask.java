package com.chongba.schedule.cron;

import com.chongba.cache.CacheService;
import com.chongba.entity.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 16:18
 */
@Slf4j
@Component
public class RefreshTask {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ThreadPoolTaskExecutor myThreadPool;

    @Scheduled(cron = "*/1 * * * * ?")
    public void refesh() {

        myThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Set<String> keys = cacheService.scan(Constants.FUTURE + "*");

                for (String key : keys) {
                    String topicKey = Constants.TOPIC + key.split(Constants.FUTURE)[1];
                    log.debug("key:{}", topicKey);

                    Set<String> values = cacheService.zRangeByScore(key, 0, System.currentTimeMillis());
                    log.debug("获取数据：{}", values.size());
                    if (!values.isEmpty() && values.size() > 0) {
                        cacheService.refreshWithPipeline(key, topicKey, values);
                        log.warn("成功的将{}定时刷新到{}", key, topicKey);
                    }
                }
            }
        });


    }

}
