package com.phoenix.security.core.social.weixin.connect;

import com.phoenix.security.core.social.weixin.api.Weixin;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * User: sheng
 * Date: 2018-04-03 21:23
 * Description: 微信连接工厂
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinServiceProvider(appId,appSecret), new WeixinAdapter());
    }

    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<Weixin>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<Weixin> createConnection(ConnectionData data) {
        return new OAuth2Connection<Weixin>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，在WeixinOAuth2Template的postForAccessGrant()方法中我们已经将openId添加到WeixinAccessGrant中，
     * 所以我们从此方法中返回WeixinAccessGrant中的openId，供其它地方调用
     * @param accessGrant
     * @return
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeixinAccessGrant) {
            WeixinAccessGrant weixinAccessGrant = (WeixinAccessGrant) accessGrant;
            return weixinAccessGrant.getOpenId();
        }
        return null;
    }

    /**
     * 构造WeixinAdapter实例
     * @param openId
     * @return
     */
    private ApiAdapter<Weixin> getApiAdapter(String openId) {
        return new WeixinAdapter(openId);
    }

    private OAuth2ServiceProvider<Weixin> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
    }

}
