package com.phoenix.security.core.properties;

import com.phoenix.security.core.validate.code.LoginType;

public class BrowserProperties {

    /**
     * 默认的登陆页
     */
    private String loginPage = "/user-login.html";

    /**
     * 登陆方式
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 默认的注册页
     */
    private String signUpUrl = "/user-signup.html";

    /**
     * 用户提交退出请求的url
     */
    private String logoutUrl = "/logout";

    /**
     * 用户退出成功时的信息提示页，
     */
    private String logoutSuccessUrl;

    /**
     * 登陆成功后，'记住我'的时间，单位为秒
     */
    private int rememberMeSeconds = 3600;

    /**
     * session配置项
     */
    private SessionProperties session = new SessionProperties();

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }
}
