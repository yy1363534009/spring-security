package com.mengxuegu.security.authentication.mobile;

import com.mengxuegu.security.authentication.CustomAuthenticationFailureHandler;
import com.mengxuegu.security.authentication.exception.ValidateCodeException;
import com.mengxuegu.security.controller.CustomMobileController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验用户登录手机验证码是否正确过滤器
 * @Auther: yuyue
 * @create 2020/6/28 14:33
 */
@Component("mobileValidaterFilter")
public class MobileValidaterFilter extends OncePerRequestFilter { // OncePerRequestFilter:所有请求被调用之前调用一次，验证码校验，在校验登录账号和密码

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 只有在用户密码校验前才能进行验证码校验
        // 1验证是否是为用户登录提交表单登录请求，登录请求是否为POST方式
        if ("/mobile/form".equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod())) {
            // 验证用户输入验证码和session中自动生成的图形验证码是否匹配
            try {
                codeVaildate(request);
            } catch (AuthenticationException e) {
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private void codeVaildate(HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute(CustomMobileController.SESSION_KEY);
        String inputCode = request.getParameter("code");
        if (StringUtils.isBlank(inputCode)) {
            throw new ValidateCodeException("越：手机验证码不能为空！");
        }
        if (!sessionCode.equalsIgnoreCase(inputCode)) {
            throw new ValidateCodeException("越：手机验证码输入错误！");
        }
    }

}
