package com.chongba.schedule.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:31
 */
public class ThreadPoolExcutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {


        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5,
                100,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 100; i++) {
            threadPool.execute(new Mytask(i));
        }

        Future<String> submit = threadPool.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "哈哈";
            }
        });
        String s = submit.get();
        System.out.println(s);

        List<Callable<String>> taskLists = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            taskLists.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "hello "+ finalI;
                }
            });
        }
        List<Future<String>> futures = threadPool.invokeAll(taskLists);
        for (Future<String> future : futures) {
            System.out.println(future.get(5,TimeUnit.SECONDS));
        }

    }
}

class Mytask implements Runnable{
    private int taskNo;

    public Mytask(int taskNo) {
        this.taskNo = taskNo;
    }

    @Override
    public void run() {
        System.out.println("execute my task" + taskNo + Thread.currentThread().getName());
    }
}
