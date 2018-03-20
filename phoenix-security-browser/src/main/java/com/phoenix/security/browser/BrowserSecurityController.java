package com.phoenix.security.browser;

import com.phoenix.security.browser.support.SimpleResponse;
import com.phoenix.security.core.properties.SecurityProperties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BrowserSecurityController {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    private RequestCache mRequestCache = new HttpSessionRequestCache();

    private RedirectStrategy mRedirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties mSecurityProperties;

    @RequestMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SavedRequest savedRequest = mRequestCache.getRequest(request,response);
        if(savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            mLogger.info("引发跳转的请求是："+redirectUrl);
            if(StringUtils.endsWithIgnoreCase(redirectUrl,".html")) {
                mRedirectStrategy.sendRedirect(request,response,mSecurityProperties.getBrowser().getLoginPage());
            }
        }

        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登陆页");
    }

}
