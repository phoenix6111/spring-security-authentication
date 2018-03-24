package com.phoenix.security.core.properties;

/**
 * 图形验证码的配置property
 */
public class ImageCodeProperties extends SmsCodeProperties{

    //验证码的宽度width
    private int width = 67;
    //验证码的高度height
    private int height = 23;

    public ImageCodeProperties() {
        //设置图片验证码的默认长度为4
        setLength(4);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
