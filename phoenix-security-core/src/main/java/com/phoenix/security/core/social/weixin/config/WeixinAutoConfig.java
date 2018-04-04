package com.phoenix.security.core.social.weixin.config;

import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.properties.WeixinProperties;
import com.phoenix.security.core.social.PhoenixConnectView;
import com.phoenix.security.core.social.weixin.connect.WeixinConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * User: sheng
 * Date: 2018-04-03 21:50
 * Description:微信登录配置
 */
@Configuration
@ConditionalOnProperty(prefix = "phoenix.security.social.weixin",name = "app-id")//只有系统配置了app-id时，WeixinAutoConfig才生效
public class WeixinAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixin = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixin.getProviderId(),weixin.getAppId(),weixin.getAppSecret());
    }

    @Bean({"connect/weixinConnect", "connect/weixinConnected"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new PhoenixConnectView();
    }
}
