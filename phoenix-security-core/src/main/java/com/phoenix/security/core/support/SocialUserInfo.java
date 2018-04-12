package com.phoenix.security.core.support;

/**
 * User: sheng
 * Date: 2018-04-01 23:43
 * Description:社交用户信息
 */
public class SocialUserInfo {

    private String proverId;

    private String providerUserId;
    //用户昵称
    private String nickname;
    //用户头像
    private String avatar;

    public String getProverId() {
        return proverId;
    }

    public void setProverId(String proverId) {
        this.proverId = proverId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
