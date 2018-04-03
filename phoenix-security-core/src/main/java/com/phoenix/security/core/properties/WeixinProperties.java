package com.phoenix.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * User: sheng
 * Date: 2018-04-03 21:52
 * Description: 微信配置
 */
public class WeixinProperties extends SocialProperties {

    //服务提供商名称，默认为weixin
    private String providerId = "weixin";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
