package com.phoenix.security.app;

import com.phoenix.security.core.social.PhoenixSpringSocialConfigurer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * User: sheng
 * Date: 2018-04-09 18:17
 * Description:
 */
@Component
public class SpringSocialConfigurerPostProcess  implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(StringUtils.equals(beanName,"phoenixSecuritySocialConfig")) {
            PhoenixSpringSocialConfigurer configurer = (PhoenixSpringSocialConfigurer) bean;
            //在App环境下，设置signup url
            configurer.signupUrl("/social/signUp");
            return configurer;
        }

        return bean;
    }
}
