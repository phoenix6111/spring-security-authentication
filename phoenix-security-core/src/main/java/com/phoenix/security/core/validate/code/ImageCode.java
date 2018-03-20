package com.phoenix.security.core.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 封装图形验证码
 */
public class ImageCode {

    private BufferedImage image;

    private String code;

    //过期时间
    private LocalDateTime expireTime;

    /**
     *
     * @param image
     * @param code
     * @param expireIn 过多少秒后过期
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     *
     * @param image
     * @param code
     * @param expireTime 过期时间
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
