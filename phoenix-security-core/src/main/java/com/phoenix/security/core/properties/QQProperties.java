package com.phoenix.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * User: sheng
 * Date: 2018-03-30 23:27
 * Description:QQ配置properties
 */
public class QQProperties extends SocialProperties {

    //服务提供商名称，默认为qq
    private String providerId = "qq";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
