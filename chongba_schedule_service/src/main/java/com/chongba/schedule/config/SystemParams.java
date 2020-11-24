package com.chongba.schedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 19:30
 */

@Data
@ConfigurationProperties(prefix="chongba")
@Component
public class SystemParams {

    private int preLoad;
}
