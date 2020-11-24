package com.chongba.schedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 13:56
 */
@EnableAsync
@EnableScheduling
@ComponentScan({"com.chongba.cache","com.chongba.schedule"})
@MapperScan("com.chongba.schedule.mapper")
@SpringBootApplication
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }
}
