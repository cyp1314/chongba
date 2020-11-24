package com.chongba.entity;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 常量工具类
 * @date 2020-11-23 19:19
 */
public class Constants {
    public static final int SCHEDULED = 0;  // 初始化状态
    public static final int EXECUTED = 1;  //  已执行状态
    public static final int CANCELLED = 2;  // 已取消状态

    public static String DBCACHE="db_cache";

    public static String FUTURE="future_";
    public static String TOPIC="topic_";
}
