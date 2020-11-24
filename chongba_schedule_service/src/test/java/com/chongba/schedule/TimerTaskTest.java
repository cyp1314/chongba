package com.chongba.schedule;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 14:01
 */
public class TimerTaskTest {

    public static void main(String[] args) {

        Timer timer = new Timer();

        // 一秒之后执行任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() / 1000 + "执行了任务");
            }
        }, 1000L);
        System.out.println(System.currentTimeMillis() / 1000);

        // 执行时间《= 当前时间 则任务立马执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() / 1000 + "甲 执行了任务");
            }
        }, new Date(System.currentTimeMillis() - 1000L));
        System.out.println(System.currentTimeMillis() / 1000);

        // 延迟一秒之后再次执行任务，然后每隔两秒再执行任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() / 1000 + "已 执行了任务");
            }
        }, 1000L, 2000L);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() / 1000 + "丙 执行了任务");
            }
        },new Date(System.currentTimeMillis() - 1000L),2000L);


        // 计算过期执行的次数且执行，然后每隔一秒执行一次
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("---");
            }
        },new Date(System.currentTimeMillis() - 3000L),1000L);
    }
}
