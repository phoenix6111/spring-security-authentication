package com.phoenix.security.app.social.openid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.HashSet;
import java.util.Set;

/**
 * User: sheng
 * Date: 2018-04-08 15:40
 * Description:
 */
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UsersConnectionRepository usersConnectionRepository;

    private SocialUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
        //从系统存储中寻找是否存在有此providerUserId(即openId)和authenticationToken.getProviderId()的记录，如存在，则返回
        Set<String> providerUserIds = new HashSet<>();
        providerUserIds.add((String) authenticationToken.getPrincipal());

        logger.info("providerId == " + authenticationToken.getProviderId() + ", openId === "+(String) authenticationToken.getPrincipal());

        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(authenticationToken.getProviderId(),providerUserIds);

        if(CollectionUtils.isEmpty(userIds) || userIds.size() != 1) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        String userId = userIds.iterator().next();
        UserDetails user = userDetailsService.loadUserByUserId(userId);

        if(user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //重新构造一个新的OpenIdAuthenticationToken
        OpenIdAuthenticationToken authenticationTokenResult = new OpenIdAuthenticationToken(user,user.getAuthorities());
        authenticationTokenResult.setDetails(user);

        return authenticationTokenResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UsersConnectionRepository getUsersConnectionRepository() {
        return usersConnectionRepository;
    }

    public void setUsersConnectionRepository(UsersConnectionRepository usersConnectionRepository) {
        this.usersConnectionRepository = usersConnectionRepository;
    }

    public SocialUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(SocialUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
