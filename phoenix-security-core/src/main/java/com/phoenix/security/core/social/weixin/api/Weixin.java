package com.phoenix.security.core.social.weixin.api;

/**
 * User: sheng
 * Date: 2018-04-03 9:41
 * Description:
 */
public interface Weixin {

    WeixinUserInfo getUserInfo(String openId);

}
