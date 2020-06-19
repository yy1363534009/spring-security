package com.mengxuegu.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 校验用户名密码业务
 * @Auther: yuyue
 * @create 2020/6/16 22:52
 */
@Component(value = "customUserDetailsServic") //交给spring容器管理
public class CustomUserDetailsServic implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.info("当前请求认证用户名：" + userName);
        //1.根据用户名查询用户密码等（此步调service也可以调dao层）
        if (!"meng".equalsIgnoreCase(userName)) {
            throw new UsernameNotFoundException("用户名或密码不正确！！！");
        }
        //假设用户密码是1234（存在库里也是通过 PasswordEncoder 对象加密后的密码）
        String password = passwordEncoder.encode("1234");
        //2.查询用户权限信息
        //假设用户权限是ADMIN
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
        //3.封装用户信息

        //交给User（它实现了UserDetails）可以帮助我们自动校验以及为用户设置权限
        return new User(userName, password, grantedAuthorities);

    }

}
