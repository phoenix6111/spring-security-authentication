package com.phoenix.security.core.authentication;

import com.phoenix.security.core.properties.SecurityConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * User: phoenix
 * Date: 2018-03-23 0:10
 * Description:
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler phoenixAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler phoenixAuthenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(phoenixAuthenticationSuccessHandler)
                .failureHandler(phoenixAuthenticationFailureHandler);
    }

}
