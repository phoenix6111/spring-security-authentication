package com.phoenix.security.browser.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * User: sheng
 * Date: 2018-04-04 18:24
 * Description: Session并发失效管理类，onExpiredSessionDetected()方法会在session并发失效时触发
 */
public class PhoenixSessionExpiredStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {
    /**
     * @param invalidSessionUrl session invalid 时跳转的url
     */
    public PhoenixSessionExpiredStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        onSessionInvalid(event.getRequest(),event.getResponse());
    }

    @Override
    protected boolean isConcurrency() {
        return true;
    }
}
