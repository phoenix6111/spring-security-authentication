package com.phoenix.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 * User: sheng
 * Date: 2018-04-02 20:10
 * Description:ConnectionSignUp接口类，实现用户经社交账号登陆后系统自动注册功能
 *
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户信息默认创建用户并返回用户唯一标识
        return connection.getDisplayName();
    }
}
