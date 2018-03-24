package com.phoenix.security.core.validate.code.image;

import com.phoenix.security.core.validate.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 封装图形验证码
 */
public class ImageCode extends ValidateCode {

    private BufferedImage image;

    /**
     *
     * @param image 验证码图片对象
     * @param code 验证码内容
     * @param expireIn 过多少秒后过期
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code,expireIn);
        this.image = image;
    }

    /**
     *
     * @param image 验证码图片对象
     * @param code 验证码内容
     * @param expireTime 过期时间
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code,expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
