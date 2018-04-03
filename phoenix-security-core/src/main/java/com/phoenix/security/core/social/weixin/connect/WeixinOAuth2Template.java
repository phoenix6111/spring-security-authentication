package com.phoenix.security.core.social.weixin.connect;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * User: sheng
 * Date: 2018-04-03 12:09
 * Description: 完成微信的OAuth2认证流程的模板类。国内厂商实现的OAuth2每个都不同, spring默认提供的OAuth2Template适应不了，只能针对每个厂商自己微调。
 */
public class WeixinOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 刷新access_token的url
     */
    private static final String URL_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    /**
     * 微信的appId
     */
    private final String clientId;

    /**
     * 微信的appSecret
     */
    private final String clientSecret;

    /**
     * 获取微信access_token的url
     */
    private final String accessTokenUrl;

    public WeixinOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);

        setUseParametersForClientAuthentication(true);

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }

    /**
     * 重写父类的exchangeForAccess方法，因为微信获取access_token时不是标准的OAuth2实现，它要求client_id为appid,client_secret为secret，
     * 所以我们要自己拼接参数
     * @param authorizationCode
     * @param redirectUri
     * @param additionalParameters
     * @return
     */
    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.set("appid", this.clientId);
        params.set("secret", this.clientSecret);
        params.set("code", authorizationCode);
        params.set("redirect_uri", redirectUri);
        params.set("grant_type", "authorization_code");
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }


        return postForAccessGrant(accessTokenUrl, params);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.set("appid", this.clientId);
        params.set("grant_type", "refresh_token");
        params.set("refresh_token", refreshToken);
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }

        return postForAccessGrant(URL_REFRESH_TOKEN, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        logger.info("获取access_token, 请求URL: "+accessTokenUrl);

        String response = getRestTemplate().postForObject(accessTokenUrl,parameters,String.class);

        logger.info("获取access_token, 响应内容: "+response);

        Map<String,Object> result = null;
        try {
            result = new ObjectMapper().readValue(response, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 根据微信文档，如果请求错误，则会返回{"errcode":40029,"errmsg":"invalid code"}的json数据，此时该方法将返回null
         */
        if(StringUtils.isNotBlank(MapUtils.getString(result,"errcode"))) {
            String errcode = MapUtils.getString(result,"errcode");
            String errmsg = MapUtils.getString(result,"errmsg");
            //抛出RuntimeException
            throw new RuntimeException("获取access token失败, errcode:"+errcode+", errmsg:"+errmsg);
        }

        return new WeixinAccessGrant(
                MapUtils.getString(result,"access_token"),
                MapUtils.getString(result,"scope"),
                MapUtils.getString(result,"refresh_token"),
                MapUtils.getLong(result,"expires_in"),
                MapUtils.getString(result,"openid")
        );
    }

    /**
     * 构建获取授权码的请求。也就是引导用户跳转到微信的地址。
     */
    @Override
    public String buildAuthenticateUrl(OAuth2Parameters parameters) {
        String url = super.buildAuthenticateUrl(parameters);
        url = url + "&appid="+clientId+"&scope=snsapi_login";
        return url;
    }

    @Override
    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        return buildAuthenticateUrl(parameters);
    }

    /**
     * 微信返回的contentType是html/text，添加相应的StringHttpMessageConverter来处理。
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        
        return restTemplate;
    }
}
