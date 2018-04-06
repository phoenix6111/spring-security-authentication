package com.phoenix.security.core.properties;

/**
 * User: sheng
 * Date: 2018-04-05 15:05
 * Description: session配置值
 */
public class SessionProperties {

    /**
     * 同一个用户在系统中的最大session数，默认1
     */
    private int maximumSessions = 1;

    /**
     * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
     */
    private boolean maxSessionsPreventLogin = false;

    /**
     * session失效时跳转的地址
     */
    private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;

    public int getMaximumSessions() {
        return maximumSessions;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public boolean isMaxSessionsPreventLogin() {
        return maxSessionsPreventLogin;
    }

    public void setMaxSessionsPreventLogin(boolean maxSessionsPreventLogin) {
        this.maxSessionsPreventLogin = maxSessionsPreventLogin;
    }

    public String getSessionInvalidUrl() {
        return sessionInvalidUrl;
    }

    public void setSessionInvalidUrl(String sessionInvalidUrl) {
        this.sessionInvalidUrl = sessionInvalidUrl;
    }
}
