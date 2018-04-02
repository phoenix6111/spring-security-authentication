package com.phoenix.security.core.social.qq.connect;

import com.phoenix.security.core.social.qq.api.QQ;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * User: sheng
 * Date: 2018-03-30 0:48
 * Description:
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId,appSecret), new QQAdapter());
    }
}
