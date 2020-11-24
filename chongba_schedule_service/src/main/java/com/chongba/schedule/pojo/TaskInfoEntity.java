package com.chongba.schedule.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 17:54
 */
@Data
@ToString
@NoArgsConstructor

@TableName("taskinfo")
public class TaskInfoEntity implements Serializable {

    private static final long serialVersionUID = -622370561977780508L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long taskId;

    @TableField
    private Date executeTime;

    @TableField
    private Integer priority;

    @TableField
    private Integer taskType;

    @TableField
    private byte[] parameters;


}
