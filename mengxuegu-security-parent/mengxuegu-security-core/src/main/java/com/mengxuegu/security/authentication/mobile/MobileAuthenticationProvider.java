package com.mengxuegu.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 认证手机号是否存在，且授权
 * @Auther: yuyue
 * @create 2020/6/29 16:55
 */
public class MobileAuthenticationProvider implements AuthenticationProvider {

    /*
    封装了用户信息
     */
    private UserDetailsService userDetailsService;

    /**
     * 处理认证
     * 1.通过手机号到数据库查询用户信息（UserDetialService）
     * 2.重新构建用户信息
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        //获取MobileAuthenticationToken手机号
        String mobile = (String) mobileAuthenticationToken.getCredentials();
        //根据手机号查询数据库，判断手机号是否存在
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
        if (userDetails==null) {
            throw new AuthenticationServiceException("该手机号尚未注册");
        }

        //查询到了手机号，通过认证，重新构建MobileAuthenticationToken实例
        MobileAuthenticationToken authenticationToken=new MobileAuthenticationToken(mobile,userDetails.getAuthorities());

        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());

        return authenticationToken;

    }

    /**
     * 判定采用哪一个AuthenticationProvider
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}
