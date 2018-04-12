package com.phoenix.security.core.properties;

/**
 * User: sheng
 * Date: 2018-04-10 19:03
 * Description: PhoenixAuthorizationServerConfig中client的配置
 */
public class OAuth2Properties {

    private String jwtSignKey = "phoenix";

    private OAuth2ClientProperties[] clients = {};

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSignKey() {
        return jwtSignKey;
    }

    public void setJwtSignKey(String jwtSignKey) {
        this.jwtSignKey = jwtSignKey;
    }
}
