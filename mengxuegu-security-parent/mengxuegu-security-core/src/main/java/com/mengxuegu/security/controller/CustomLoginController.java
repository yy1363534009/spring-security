package com.mengxuegu.security.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 自定义security拦截跳转请求的处理器
 *
 * @Auther: yuyue
 * @create 2020/6/14 22:41
 */

@Controller
public class CustomLoginController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * security拦截跳转登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login/page")
    public String toLogin() {
        return "login"; //classpth:/templates/login.html
    }

    @RequestMapping(value = "/code/image")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1获取徒刑验证码
        String code = defaultKaptcha.createText();
        logger.info("生成的图片验证码是：" + code);
        // 2将验证码存放到session中，用于过滤器校验
        request.getSession().setAttribute(SESSION_KEY, code);
        // 3获取图片验证码
        BufferedImage image = defaultKaptcha.createImage(code);
        // 4将验证码写入到登录页面，用于展现
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);

    }

    @RequestMapping(value = "/exit/page")
    public String toExit(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if (cookie.getName().equals("remember-me") || cookie.getName().equals("JSESSIONID")) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return "login"; //classpth:/templates/login.html
    }

}
