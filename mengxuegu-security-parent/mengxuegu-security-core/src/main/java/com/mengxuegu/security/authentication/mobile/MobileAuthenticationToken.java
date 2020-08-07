package com.mengxuegu.security.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 封装用户手机号token
 * @Auther: yuyue
 * @create 2020/6/29 16:37
 */
public class MobileAuthenticationToken  extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 520L;
    /*
    认证之前放手机号，认证之后放用户信息
     */
    private Object credentials;

    public MobileAuthenticationToken(Object credentials) {
        super((Collection)null);
        this.credentials = credentials;//手机号
        this.setAuthenticated(false);//认证未通过
    }

    public MobileAuthenticationToken(Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;//用户信息
        super.setAuthenticated(true);//标示已经认证通过
    }

    /*
    手机号
     */
    public Object getCredentials() {

        return this.credentials;
    }

    /**
     * 父类的一个抽象方法，必须实现，他是密码，当前不需要，直接返回null
     * @return
     */
    public Object getPrincipal() {
        return this.credentials;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}