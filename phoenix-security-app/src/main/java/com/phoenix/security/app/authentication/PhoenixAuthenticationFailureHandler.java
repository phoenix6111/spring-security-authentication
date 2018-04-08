package com.phoenix.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.support.SimpleResponse;
import com.phoenix.security.core.validate.code.LoginType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("phoenixAuthenticationFailureHandler")
public class PhoenixAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties mSecurityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        //记录用户登陆成功日志
        mLogger.info("登陆失败");

        //如果是LoginType=json，则返回json，否则调用父类的方法直接跳转到错误的页面
        if(LoginType.JSON.equals(mSecurityProperties.getBrowser().getLoginType())) {
            //向客户端发送成功authentication
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(e.getMessage())));
        } else {
            super.onAuthenticationFailure(request,response,e);
        }


    }
}
