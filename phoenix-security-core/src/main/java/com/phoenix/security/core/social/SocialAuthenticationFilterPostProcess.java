package com.phoenix.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * User: sheng
 * Date: 2018-04-09 15:17
 * Description:
 */
public interface SocialAuthenticationFilterPostProcess {

    void process(SocialAuthenticationFilter socialAuthenticationFilter);

}
