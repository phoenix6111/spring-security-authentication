package com.phoenix.security.browser;

import com.phoenix.security.core.authentication.AbstractChannelSecurityConfig;
import com.phoenix.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.phoenix.security.core.properties.SecurityConstants;
import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.validate.code.ValidateCodeSecurityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

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
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ‘记住我’的token数据源
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        logger.info(String.valueOf(usersConnectionRepository));

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(dataSource);
        //启动时创建‘记住我’的数据库表
//        tokenRepository.setCreateTableOnStartup(true);

        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)
                .and()
            .apply(smsCodeAuthenticationSecurityConfig)
                .and()
            .apply(phoenixSecuritySocialConfig)
                .and()
                //处理session失效
            .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())//session最大值为1
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventLogin())//达到最大session时是否阻止新的登录请求
                .expiredSessionStrategy(sessionInformationExpiredStrategy)//session失效处理器
                .and()
                .and()
            .logout()
                .logoutUrl(securityProperties.getBrowser().getLogoutUrl())//自定义用户提交退出请求的url
                .logoutSuccessHandler(logoutSuccessHandler)//用户退出时的处理handler
                .deleteCookies("JSESSIONID")//删除cookie，参数为待删除的cookie的名字
                .and()
            //记住我的功能
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
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
                        "/user/register"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
            .csrf().disable();

    }
}
