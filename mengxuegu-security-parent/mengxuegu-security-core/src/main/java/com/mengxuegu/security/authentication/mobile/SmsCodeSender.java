package com.mengxuegu.security.authentication.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送短信验证码实现
 *
 * @Auther: yuyue
 * @create 2020/6/28 14:01
 */
public class SmsCodeSender implements SmsSend {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean sendSms(String mobile, String content) {
        String sendContent = String.format("权限管理，验证码%s，请勿泄露他人。", content);
        logger.info("向手机号" + mobile + "发送的短信为:" + sendContent);
        return true;
    }

}
