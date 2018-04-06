package com.phoenix.security.browser.session;

import org.springframework.security.web.session.InvalidSessionStrategy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: sheng
 * Date: 2018-04-05 1:23
 * Description: session失效处理
 */
public class PhoenixSessionInvalidStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {
    /**
     * @param invalidSessionUrl session invalid 时跳转的url
     */
    public PhoenixSessionInvalidStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        onSessionInvalid(request,response);
    }
}
