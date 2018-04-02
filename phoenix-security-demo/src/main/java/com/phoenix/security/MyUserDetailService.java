package com.phoenix.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService,SocialUserDetailsService {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder mPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        mLogger.info("表单登陆用户名："+username);
        //根据用户名查找用户信息

        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        mLogger.info("社交登陆用户名："+userId);
        return buildUser(userId);
    }

    /**
     * 根据username或userId查找用户
     * @param userId
     * @return
     */
    private SocialUserDetails buildUser(String userId) {
        String password = mPasswordEncoder.encode("123456");
        mLogger.info("数据库密码："+password);

        //判断用户信息是否过期，是否锁定
        return new SocialUser(userId,password,
                true,true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }


}
