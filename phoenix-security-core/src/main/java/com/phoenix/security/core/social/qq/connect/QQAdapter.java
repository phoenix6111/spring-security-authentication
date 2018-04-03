package com.phoenix.security.core.social.qq.connect;

import com.phoenix.security.core.social.qq.api.QQ;
import com.phoenix.security.core.social.qq.api.QQUserInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * User: sheng
 * Date: 2018-03-30 0:26
 * Description: QQ API 适配器，将QQ api的数据模型转为spring social的标准模型。
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 测试QQ认证是否有效
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 通过QQ api 获取用户信息，并将用户认证信息添加到ConnectionValues中
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();
        //用户昵称
        values.setDisplayName(userInfo.getNickname());
        //用户头像
        values.setImageUrl(StringUtils.isNotBlank(userInfo.getFigureurl_qq_2())? userInfo.getFigureurl_qq_2():userInfo.getFigureurl_qq_1());
        //设置主页
        values.setProfileUrl(null);
        //设置openId
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    /**
     * 更新用户状态
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQ api, String message) {

    }
}
