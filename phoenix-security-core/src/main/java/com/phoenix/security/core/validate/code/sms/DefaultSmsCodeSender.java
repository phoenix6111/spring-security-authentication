package com.phoenix.security.core.validate.code.sms;

/**
 * User: phoenix
 * Date: 2018-03-21 15:16
 * Description:
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        System.out.println("发送短信验证码到电话号码："+mobile+"，短信验证码内容："+code);
    }
}
