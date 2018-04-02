package com.phoenix.security.core.social.qq.config;

import com.phoenix.security.core.properties.QQProperties;
import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.social.qq.connect.QQConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.social.connect.ConnectionFactory;

/**
 * User: sheng
 * Date: 2018-03-30 23:33
 * Description:
 */
@Configuration
@Order(2)
@ConditionalOnProperty(prefix = "phoenix.security.social.qq",name = "app-id")//只有系统配置了app-id时，QQAutoConfig才生效
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {

        QQProperties qqConfig = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qqConfig.getProviderId(),qqConfig.getAppId(),qqConfig.getAppSecret());
    }

    /*@Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }*/
}
