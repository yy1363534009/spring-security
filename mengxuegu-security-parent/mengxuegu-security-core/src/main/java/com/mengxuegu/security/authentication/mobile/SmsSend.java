package com.mengxuegu.security.authentication.mobile;

/**
 * 短信发送服务接口
 *
 * @Auther: yuyue
 * @create 2020/6/28 13:58
 */
public interface SmsSend {

    /**
     * 短息发送
     * @param mobile 手机号
     * @param content 内容
     * @return true：发送成功，false：发送失败
     */
    boolean sendSms(String mobile, String content);

}
