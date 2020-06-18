package com.mengxuegu.security.authentication;

import com.mengxuegu.base.result.MengxueguResult;
import com.mengxuegu.security.properties.LoginResponseType;
import com.mengxuegu.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义失败处理器
 * @Auther: yuyue
 * @create 2020/6/17 18:20
 */
@Component("customAuthenticationFailureHandler")
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {  //返回json字符串，无法返回上一个请求，所以用下面的代替
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler { //继承AuthenticationFailureHandler的实现类

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            MengxueguResult result = MengxueguResult.build(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(result.toJsonString());
        } else {
            //回到上一个请求地址（登录页面）
            super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage() + "?error");//加error参数是为了让页面显示错误信息因为登录页面有param.error判断
            super.onAuthenticationFailure(request, response, exception);
        }
    }

}
