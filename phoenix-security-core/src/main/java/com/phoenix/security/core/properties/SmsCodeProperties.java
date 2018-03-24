package com.phoenix.security.core.properties;

/**
 * User: phoenix
 * Date: 2018-03-21 15:01
 * Description: 短信验证码的property配置类，同时也是图片验证码Property的父类
 */
public class SmsCodeProperties {

    //验证码的长度length
    private int length = 6;
    //验证码的有效时间
    private int expireIn = 60;
    //需要做验证码校验的url
    private String urls;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
