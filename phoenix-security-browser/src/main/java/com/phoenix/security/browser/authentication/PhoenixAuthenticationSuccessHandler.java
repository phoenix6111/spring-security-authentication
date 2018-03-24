package com.phoenix.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.core.validate.code.LoginType;
import com.phoenix.security.core.properties.SecurityProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("phoenixAuthenticationSuccessHandler")
public class PhoenixAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties mSecurityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //记录用户登陆成功日志
        mLogger.info("登陆成功");

        //如果是LoginType=json，则返回json，否则调用父类的方法直接跳转到请求的url
        if(LoginType.JSON.equals(mSecurityProperties.getBrowser().getLoginType())) {
            //向客户端发送成功authentication
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            super.onAuthenticationSuccess(request,response,authentication);
        }

    }
}
