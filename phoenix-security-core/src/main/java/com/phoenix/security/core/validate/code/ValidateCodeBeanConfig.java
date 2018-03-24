package com.phoenix.security.core.validate.code;

import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.validate.code.image.ImageCodeGenerator;
import com.phoenix.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.phoenix.security.core.validate.code.sms.SmsCodeSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties mSecurityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator generator = new ImageCodeGenerator();
        generator.setSecurityProperties(mSecurityProperties);

        return generator;
    }

    /**
     * 配置SmsCodeSender的默认实现类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
