package com.mengxuegu.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 加载认证信息配置类
 * @Auther: yuyue
 * @create 2020/6/15 21:52
 */
@Configuration
public class ReloadMessageConfig {

    /**
     * 加载中文提示信息
     * @return
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        // messages_zh_CN.properties 不需要后缀以为会自动拼接
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        return messageSource;
    }

}
