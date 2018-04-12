package com.phoenix.security.core.properties;

/**
 * User: sheng
 * Date: 2018-04-10 19:03
 * Description:
 */
public class OAuth2ClientProperties {

    private String clientId;

    private String clientSecret;

    //如果accessTokenValiditySeconds的值为0，则token的有效期为无限期
    private int accessTokenValiditySeconds = 3600;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }
}
