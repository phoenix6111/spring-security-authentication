package com.phoenix.security.browser.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.security.browser.support.SimpleResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: sheng
 * Date: 2018-04-05 1:00
 * Description:
 */
public class AbstractSessionStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 跳转的url
     */
    private String destinationUrl;

    /**
     * 重定向策略
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 跳转前是否创建新的session
     */
    private boolean createNewSession = true;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param invalidSessionUrl session invalid 时跳转的url
     */
    public AbstractSessionStrategy(String invalidSessionUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
        this.destinationUrl = invalidSessionUrl;
    }

    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("session失效");

        /**
         * 如果跳转前需要创建新的session，则创建新的session
         */
        if (createNewSession) {
            request.getSession();
        }

        String sourceUrl = request.getRequestURI();
        //如果是html形式的请求，则返回html结果页面，否则返回json结果页面
        if(StringUtils.endsWith(sourceUrl,".html")) {
            String targetUrl = this.destinationUrl;
            logger.info("session失效,跳转到"+targetUrl);
            redirectStrategy.sendRedirect(request,response,targetUrl);
        } else {
            String message = "session已失效";
            if(isConcurrency()){
                message = message + "，有可能是并发登录导致的";
            }

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(message)));
        }

    }

    /**
     * session失效是否是并发导致的
     * @return
     */
    protected boolean isConcurrency() {
        return false;
    }

    /**
     * Determines whether a new session should be created before redirecting (to
     * avoid possible looping issues where the same session ID is sent with the
     * redirected request). Alternatively, ensure that the configured URL does
     * not pass through the {@code SessionManagementFilter}.
     *
     * @param createNewSession
     *            defaults to {@code true}.
     */
    public void setCreateNewSession(boolean createNewSession) {
        this.createNewSession = createNewSession;
    }
}
