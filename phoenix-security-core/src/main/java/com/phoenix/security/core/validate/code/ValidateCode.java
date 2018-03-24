package com.phoenix.security.core.validate.code;

import java.time.LocalDateTime;

/**
 * User: phoenix
 * Date: 2018-03-21 14:52
 * Description: 验证码的父类，同时也是短信验证码类
 */
public class ValidateCode {

    //验证码内容
    private String code;

    //过期时间
    private LocalDateTime expireTime;

    /**
     *
     * @param code 验证码数字
     * @param expireIn 过多少秒后过期
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     *
     * @param code 验证码数字
     * @param expireTime 过期时间
     */
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }


    /**
     * 判断验证码是否过期
     * @return
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
