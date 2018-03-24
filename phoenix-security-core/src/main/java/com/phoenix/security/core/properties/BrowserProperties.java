package com.phoenix.security.core.properties;

import com.phoenix.security.core.validate.code.LoginType;

public class BrowserProperties {

    //默认的登陆页
    private String loginPage = "/user-login.html";

    //登陆方式
    private LoginType loginType = LoginType.JSON;

    //登陆成功后，'记住我'的时间，单位为秒
    private int rememberMeSeconds = 3600;

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
}
