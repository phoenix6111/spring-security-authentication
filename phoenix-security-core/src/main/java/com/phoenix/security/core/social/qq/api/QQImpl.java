package com.phoenix.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * User: sheng
 * Date: 2018-03-29 21:13
 * Description:
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //获取openid的url
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    //获取用户信息API的url
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String openId;
    private String appId;

    //解析String to Object
    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        String getOpenUrl = String.format(URL_GET_OPENID,accessToken);
        String result = getRestTemplate().getForObject(getOpenUrl,String.class);
        //获取openid时，返回值如下：callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
        logger.info(result);
        //裁剪返回的字符串，截取openid值
        this.openId = StringUtils.substringBetween(result,"\"openid\":\"","\"}");
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public QQUserInfo getUserInfo() {
        String getUserInfoUrl = String.format(URL_GET_USERINFO,this.appId,this.openId);
        String result = getRestTemplate().getForObject(getUserInfoUrl,String.class);

        logger.info(result);

        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result,QQUserInfo.class);
            userInfo.setOpenId(this.openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息异常",e);
        }
    }
}
