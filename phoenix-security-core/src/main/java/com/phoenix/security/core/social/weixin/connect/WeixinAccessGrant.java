package com.phoenix.security.core.social.weixin.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * User: sheng
 * Date: 2018-04-03 17:59
 * Description: 自定义一个WeixinAccessGrant类继承自AccessGrant，因为微信的特殊性，它的openId是和access_token一起返回的，
 * 所以它的openId要保存在AccessGrant中，所以在WeixinAccessGrant添加了一个属性openId
 */
public class WeixinAccessGrant extends AccessGrant {

    /**
     * 微信的openid
     */
    private String openId;

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn,String openId) {
        super(accessToken,scope,refreshToken,expiresIn);
        this.openId = openId;
    }

    public String getOpenId() {
        return openId;
    }
}
