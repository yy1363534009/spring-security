package com.mengxuegu.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Auther: yuyue
 * @create 2020/6/29 16:55
 */
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 处理认证
     * 1.通过手机号到数据库查询用户信息（UserDetialService）
     * 2.重新构建用户信息
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    /**
     * 判定采用哪一个AuthenticationProvider
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}
