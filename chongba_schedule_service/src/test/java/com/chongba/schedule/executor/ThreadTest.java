package com.chongba.schedule.executor;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:23
 */
public class ThreadTest {

    public static void main(String[] args) {

        myThread myThread = new myThread();
        myThread.setName("t1");
        myThread.start();

        myThread myThread1 = new myThread();
        myThread1.setName("t2");
        myThread1.start();

        myRunnable myRunnable = new myRunnable();
        Thread t1 = new Thread(myRunnable);
        t1.setName("r1");
        t1.start();

    }
}

class myRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+System.currentTimeMillis());
    }
}

class myThread extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+System.currentTimeMillis());
    }
}
