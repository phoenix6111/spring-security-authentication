package com.phoenix.security.core.social.weixin.connect;

import com.phoenix.security.core.social.weixin.api.Weixin;
import com.phoenix.security.core.social.weixin.api.WeixinUserInfo;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * User: sheng
 * Date: 2018-04-03 18:20
 * Description:微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinAdapter() { }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin api) {
        return true;
    }

    /**
     * 通过Weixin api 获取用户信息，并将用户认证信息添加到ConnectionValues中
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo userInfo = api.getUserInfo(this.openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {

    }
}
