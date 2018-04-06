package com.phoenix.security.browser;

import com.phoenix.security.browser.logout.PhoenixLogoutSuccessHandler;
import com.phoenix.security.browser.session.PhoenixSessionExpiredStrategy;
import com.phoenix.security.browser.session.PhoenixSessionInvalidStrategy;
import com.phoenix.security.core.properties.SecurityProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * User: sheng
 * Date: 2018-04-05 1:25
 * Description:
 */
@Configuration
public class BrowserSecurityBeanConfig  {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * session失效处理bean，增加@ConditionalOnMissingBean注解可以让用户自定义InvalidSessionStrategy实现覆盖我们实现的PhoenixSessionInvalidStrategy
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new PhoenixSessionInvalidStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

    /**
     * session因为并发而下线处理bean，增加@ConditionalOnMissingBean注解可以让用户自定义SessionInformationExpiredStrategy实现覆盖我们实现的PhoenixSessionExpiredStrategy
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new PhoenixSessionExpiredStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

    /**
     * 用户退出成功时处理handler，增加@ConditionalOnMissingBean注解可以让用户自定义LogoutSuccessHandler实现覆盖我们实现的PhoenixLogoutSuccessHandler
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new PhoenixLogoutSuccessHandler(securityProperties.getBrowser().getLogoutSuccessUrl());
    }

}
