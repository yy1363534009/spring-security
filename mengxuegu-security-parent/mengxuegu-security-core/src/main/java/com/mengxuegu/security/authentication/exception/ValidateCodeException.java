package com.mengxuegu.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常Exception
 * @Auther: yuyue
 * @create 2020/6/19 23:42
 */
public class ValidateCodeException extends AuthenticationException {//继承AuthenticationException可以提供页面自定义异常错误信息

    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
