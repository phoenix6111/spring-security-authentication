package com.phoenix.security.core.social.weixin.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * User: sheng
 * Date: 2018-04-03 9:42
 * Description:
 */
public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 本来微信获取用户信息的url是https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID，但我们在构造函数中
     * 设置了super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER)，则spring social会自动将access_token作为参数添加到我们的URL_GET_USER_INFO上
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinImpl(String accessToken) {
        /**
         * 设置了super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER)，则spring social会自动将access_token作为参数添加到我们的URL_GET_USER_INFO上
         */
        super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String userInfoRequestUrl = URL_GET_USER_INFO + openId;
        String result = getRestTemplate().getForObject(userInfoRequestUrl,String.class);

        /**
         * 判断微信是否返回错误信息，如果返回错误信息，则直接返回null
         */
        if(StringUtils.contains(result,"errcode")) {
            logger.info("获取微信用户信息出现错误");
            return null;
        }

        WeixinUserInfo userInfo = null;

        try {
            userInfo = objectMapper.readValue(result,WeixinUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取微信用户信息异常",e);
        }

        return userInfo;
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        //删除第一个StringHttpMessageConverter
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return messageConverters;
    }
}
