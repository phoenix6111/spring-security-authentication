package com.phoenix.security.core.social.qq.connect;

import com.phoenix.security.core.social.qq.api.QQ;
import com.phoenix.security.core.social.qq.api.QQImpl;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * User: sheng
 * Date: 2018-03-30 0:00
 * Description:
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    /**
     * 获取Authorization Code的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 通过Authorization Code获取Access Token的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    private String appId;

    public QQServiceProvider(String appId,String appSecret) {
        super(new QQOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken,this.appId);
    }
}
