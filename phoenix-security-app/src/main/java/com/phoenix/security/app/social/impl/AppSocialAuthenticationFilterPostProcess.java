package com.phoenix.security.app.social.impl;

import com.phoenix.security.core.social.SocialAuthenticationFilterPostProcess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * User: sheng
 * Date: 2018-04-09 15:19
 * Description:
 */
public class AppSocialAuthenticationFilterPostProcess implements SocialAuthenticationFilterPostProcess {

    @Autowired
    private AuthenticationSuccessHandler phoenixAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        socialAuthenticationFilter.setAuthenticationSuccessHandler(phoenixAuthenticationSuccessHandler);
    }
}
