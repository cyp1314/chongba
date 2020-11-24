package com.chongba.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 19:14
 */
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = -6197420635991614651L;

    // 编号
    private Long taskId;

    // 类型
    private Integer taskType;

    // 优先级
    private Integer priority;

    // 执行id
    private long executeTime;

    // 参数
    private byte[] parameters;


}
