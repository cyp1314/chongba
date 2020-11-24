package com.chongba.schedule.jdk;

import java.util.concurrent.DelayQueue;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 17:31
 */
public class DelatQueueTest {

    public static void main(String[] args) {


        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        queue.add(new DelayedTask(5,"甲"));
        queue.add(new DelayedTask(10,"乙"));
        queue.add(new DelayedTask(15,"丙"));

        System.out.println(System.currentTimeMillis()/1000 + " start consume ");


        while (queue.size()!=0){
            DelayedTask delayedTask = queue.poll();

            if (delayedTask!=null){
                System.out.println(System.currentTimeMillis()/1000 + delayedTask.getTaskName() + "  consume task");
            }

            try {
                Thread.sleep(1000L);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }




    }
}
