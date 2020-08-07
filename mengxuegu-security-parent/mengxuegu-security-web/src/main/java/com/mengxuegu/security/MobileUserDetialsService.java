package com.mengxuegu.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Auther: yuyue
 * @create 2020/6/30 14:05
 */
@Component("mobileUserDetialsService")
public class MobileUserDetialsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        System.out.println("MobileUserDetialsService mobile：" + mobile);
        logger.info("用户输入的手机号是" + mobile);
        // 通过手机号查询用户信息

        //用户信息为null，抛出异常，跳转失败处理器进行处理

        //用户信息不为null，封装用户信息和用户权限(remember再次进入时 是用CustomUserDetailsServic进行校验)
        return new User("meng", "", true, true
                , true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
    }

}
