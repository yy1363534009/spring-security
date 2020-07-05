package com.mengxuegu.security.authentication;

import com.mengxuegu.base.result.MengxueguResult;
import com.mengxuegu.security.properties.LoginResponseType;
import com.mengxuegu.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger= LoggerFactory.getLogger(getClass());

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
            //获取上一次的请求url
            String referer = request.getHeader("Referer");
            logger.info("Referer:"+referer);
            String lastUrl = StringUtils.substringBefore(referer, "?");
            logger.info("上一次请求的url："+lastUrl);
            //为了短信验证码登录失败回到上一次请求url，而不是注视掉的，采用动态设置。
            super.setDefaultFailureUrl(lastUrl+"?error");
//            super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage() + "?error");//加error参数是为了让页面显示错误信息因为登录页面有param.error判断
            super.onAuthenticationFailure(request, response, exception);
        }
    }

}
