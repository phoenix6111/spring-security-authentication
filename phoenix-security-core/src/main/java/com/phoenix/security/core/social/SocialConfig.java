package com.phoenix.security.core.social;

import com.phoenix.security.core.properties.SecurityProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * User: sheng
 * Date: 2018-03-30 16:37
 * Description: 社交登陆配置
 */
@Configuration
@EnableSocial
@Order(1)
/**
 * 添加@Order(1)注解，使SocialConfig的配置在其它配置前，具体原因见org.springframework.social.config.annotation.SocialConfiguration类的usersConnectionRepository()
 * 方法，在usersConnectionRepository()方法中，它会取出所有SocialConfigurer的实现类的集合，然后for循环调用每个SocialConfigurer的实现类获取UsersConnectionRepository，
 * 如果获取的UsersConnectionRepository不为空，则停止循环，然后因为SocialConfigurerAdapter中默认实现了getUsersConnectionRepository()方法，返回的是InMemoryUsersConnectionRepository对象，
 * 所以如果其它配置类如QQAutoConfig继承了SocialConfigurerAdapter或它的子类，则它也实现了getUsersConnectionRepository()方法，所以如果QQAutoConfig在spring容器的前部，则
 * 在org.springframework.social.config.annotation.SocialConfiguration类的usersConnectionRepository()方法中，首先得到InMemoryUsersConnectionRepository，退出循环，
 * 然后后面你自己配置的JdbcUsersConnectionRepository也不会生效，SpringSocial使用的也是InMemoryUsersConnectionRepository，
 * 所以应该将SocialConfig配置放在前面
 *
 */
public class SocialConfig extends SocialConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //构建UserConnection Repository，通过UserConnectionRepository操作数据库表UserConnection，
        // 建表语句在spring-social-core下的org.springframework.social.connect.jdbc包下的JdbcUsersConnectionRepository.sql文件中
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator,Encryptors.noOpText());
        //数据库表名不能更改，但可以设置数据库表前缀
        repository.setTablePrefix("");

        /**
         * 实现用户经社交账号登陆后系统自动注册功能，见org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository.findUserIdsWithConnection(Connection<?> connection)方法
         * 如果实现了ConnectionSignUp接口类的成员变量connectionSignUp不为空且从数据库里查不出数据，它就会调用connectionSignUp的connectionSignUp.execute(connection)生成用户唯一标识，
         * 并用该唯一标识作为userId，生成一条数据UserConnection表中的记录。实现社交账号一键登录
         *
         */
        if(connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }

        return repository;
    }

    /**
     * SpringSocialConfig配置，拦截社交登陆请求
     * @return
     */
    @Bean
    public SpringSocialConfigurer phoenixSecuritySocialConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        PhoenixSpringSocialConfigurer configurer = new PhoenixSpringSocialConfigurer(filterProcessesUrl);
        //设置注册页
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());

        return configurer;
    }

    /**
     * 对外提供一个ProviderSignInUtils，通过该ProviderSignInUtils可以获取用户社交登陆后的用户信息
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
    }
}
