package com.mengxuegu.security.controller;

import com.mengxuegu.base.result.MengxueguResult;
import com.mengxuegu.security.authentication.mobile.SmsSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 手机登录控制层
 *
 * @Auther: yuyue
 * @create 2020/6/28 14:15
 */
@Controller
public class CustomMobileController {

    public static final String SESSION_KEY = "SESSION_KEY_MOBILE_CODE";

    @Autowired
    private SmsSend smsSend;

    /**
     * 前往手机验证码登录页 * @return
     */
    @RequestMapping("/mobile/page")
    public String toMobilePage() {
        return "login-mobile"; // templates/login-mobile.html
    }

    /**
     * 发送手机验证码
     *
     * @param request
     * @return
     */
    @RequestMapping("/code/mobile")
    @ResponseBody
    public MengxueguResult smsCode(HttpServletRequest request) {
        // 生成四位的手机验证码
        //String code = RandomStringUtils.randomNumeric(4);
        String code = "6666";
        // 将验证码保存到session中，用于后面校验在验证码
        request.getSession().setAttribute(SESSION_KEY, code);
        // 将验证码发送给用户手机
        smsSend.sendSms(request.getParameter("mobile"), code);

        return MengxueguResult.ok();
    }

}
