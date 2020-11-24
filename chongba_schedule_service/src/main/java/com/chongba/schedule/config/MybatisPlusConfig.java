package com.chongba.schedule.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-23 18:14
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {
    
    /** 
     * @description: 支持乐观锁
     * @param: * @param:  
     * @return: com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor 
     * @author Mr-CHEN
     * @date: 2020-11-23 18:16
     */ 
    @Bean
    public OptimisticLockerInterceptor OptimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

//    @Bean
//    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor(){
//        return new OptimisticLockerInnerInterceptor();
//    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
