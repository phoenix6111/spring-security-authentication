package com.phoenix.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * User: phoenix
 * Date: 2018-03-23 20:12
 * Description:
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler phoenixAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler phoenixAuthenticationFailureHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter authenticationFilter = new SmsCodeAuthenticationFilter();
        //设置AuthenticationManager
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置success和failure处理器
        authenticationFilter.setAuthenticationSuccessHandler(phoenixAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(phoenixAuthenticationFailureHandler);

        SmsCodeAuthenticationProvider authenticationProvider = new SmsCodeAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(authenticationProvider)
            .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
