package com.phoenix.security.app;

import com.phoenix.security.app.social.AppSignUpUtils;
import com.phoenix.security.core.support.SocialUserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * User: sheng
 * Date: 2018-04-10 12:26
 * Description:
 */
@RestController
public class AppSecurityController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private AppSignUpUtils appSignUpUtils;

    /**
     * 通过社交账号注册时，获取用户社交账号信息
     * @return
     */
    @GetMapping("/social/signUp")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        SocialUserInfo userInfo = new SocialUserInfo();
        /**
         * 在APP环境下，每一次请求都会创建一个新的session，我们可以在跳转之前，把connection数据从session中拿出来，放到redis中
         */
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        if(connection != null) {
            userInfo.setProverId(connection.getKey().getProviderId());
            userInfo.setProviderUserId(connection.getKey().getProviderUserId());
            userInfo.setNickname(connection.getDisplayName());
            userInfo.setAvatar(connection.getImageUrl());
        }

        appSignUpUtils.saveConnectionData(connection.createData(),new ServletWebRequest(request));

        return userInfo;
    }

}
