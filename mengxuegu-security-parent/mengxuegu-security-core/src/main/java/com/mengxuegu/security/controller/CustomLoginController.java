package com.mengxuegu.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 自定义security拦截跳转请求的处理器
 * @Auther: yuyue
 * @create 2020/6/14 22:41
 */

@Controller
public class CustomLoginController {

    /**
     * security拦截跳转登录页面
     * @return
     */
    @RequestMapping(value = "/login/page")
    public String toLogin(){
        return "login"; //classpth:/templates/login.html
    }

}
