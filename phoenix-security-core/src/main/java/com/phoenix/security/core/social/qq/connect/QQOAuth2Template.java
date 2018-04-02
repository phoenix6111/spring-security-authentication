package com.phoenix.security.core.social.qq.connect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * User: sheng
 * Date: 2018-04-01 18:41
 * Description:解决QQ认证特殊性而重写父类OAuth2Template
 */
public class QQOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        //在父类的exchangeForAccess方法中，只有useParametersForClientAuthentication为true，
        // 才会将client_id和client_secret作为参数去获取accessToken，而在QQ认证中获取access中，client_id和client_secret又是必须携带的参数
        //所以才将useParametersForClientAuthentication设置为true
        setUseParametersForClientAuthentication(true);

    }

    /**
     * 重写父类的postForAccessGrant方法：因为从QQ获取accessToken时返回的信息的特殊性，标准oauth2认证应该返回json数据，
     * 而QQ返回的是：access_token=FE04*************CCE2&expires_in=7776000&refresh_token=88E4***********BE14
     * 这样的字符串数据，需要把这些字符串数据取出来重新封装
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl,parameters,String.class);

        logger.info(responseStr);

        //将QQ返回的accessToken数据：access_token=FE04*************CCE2&expires_in=7776000&refresh_token=88E4***********BE14截取出来
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr,"&");
        String accessToken = StringUtils.substringAfterLast(items[0],"=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1],"="));
        String refreshToken = StringUtils.substringAfterLast(items[2],"=");

        return new AccessGrant(accessToken,null,refreshToken,expiresIn);
    }

    /**
     * 重写父类的createRestTemplate方法，因为父类的createRestTemplate方法中创建的RestTemplate没有添加字符串处理
     * 的converters StringHttpMessageConverter，所以需要将StringHttpMessageConverter添加到父类的RestTemplate的MessageConverters中
     * @return RestTemplate
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        //将StringHttpMessageConverter添加到父类的RestTemplate中
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }
}
