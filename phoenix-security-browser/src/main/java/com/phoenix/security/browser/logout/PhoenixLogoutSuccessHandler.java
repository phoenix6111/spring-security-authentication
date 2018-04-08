package com.phoenix.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.core.support.SimpleResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: sheng
 * Date: 2018-04-06 10:08
 * Description: 用户退出成功handler
 */
public class PhoenixLogoutSuccessHandler implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户退出成功时的信息提示页
     */
    private String logoutSuccessUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public PhoenixLogoutSuccessHandler(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        logger.info("用户退出成功");

        /**
         * 如果用户配置了logoutSuccessUrl，则将用户重定向到logoutSuccessUrl，否则将退出成功信息以Json形式返回
         */
        if (StringUtils.isNotBlank(this.logoutSuccessUrl)) {
            response.sendRedirect(this.logoutSuccessUrl);
        } else {
            response.setContentType("application/json;chartset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("用户退出成功")));
        }

    }
}
