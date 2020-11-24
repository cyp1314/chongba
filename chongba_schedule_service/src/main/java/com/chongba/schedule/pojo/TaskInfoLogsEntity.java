package com.chongba.schedule.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 18:11
 */
@Data
@ToString
@AutoConfigureOrder

@TableName("taskinfo_log")
public class TaskInfoLogsEntity extends TaskInfoEntity {

    @TableField
    @Version
    private Integer version;

    @TableField
    private Integer status;
}
