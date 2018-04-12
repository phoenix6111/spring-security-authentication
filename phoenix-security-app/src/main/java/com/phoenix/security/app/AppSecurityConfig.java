package com.phoenix.security.app;

import com.phoenix.security.app.social.openid.OpenIdAuthenticationConfig;
import com.phoenix.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.phoenix.security.core.properties.SecurityConstants;
import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.validate.code.ValidateCodeSecurityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * User: sheng
 * Date: 2018-04-07 15:32
 * Description:
 */
@Configuration
public class AppSecurityConfig extends ResourceServerConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SpringSocialConfigurer phoenixSecuritySocialConfig;

    @Autowired
    private AuthenticationSuccessHandler phoenixAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler phoenixAuthenticationFailureHandler;

    @Autowired
    private OpenIdAuthenticationConfig openIdAuthenticationConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(phoenixAuthenticationSuccessHandler)
                .failureHandler(phoenixAuthenticationFailureHandler);

        http
                .apply(validateCodeSecurityConfig)
                    .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                    .and()
                .apply(phoenixSecuritySocialConfig)
                    .and()
                .apply(openIdAuthenticationConfig)
                    .and()
                .authorizeRequests()
                    .antMatchers(
                            SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                            securityProperties.getBrowser().getLoginPage(),
                            securityProperties.getBrowser().getSignUpUrl(),
                            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                            securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
                            securityProperties.getBrowser().getLogoutSuccessUrl(),
                            "/user/register","/social/signUp"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .csrf().disable();

    }
}
