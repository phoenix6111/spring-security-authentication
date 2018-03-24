package com.phoenix.security.core.validate.code.sms;

import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.validate.code.ValidateCode;
import com.phoenix.security.core.validate.code.ValidateCodeGenerator;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * User: phoenix
 * Date: 2018-03-21 14:58
 * Description: 短信验证码生成器
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties mSecurityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        //随机生成6位验证码
        String code = RandomStringUtils.randomNumeric(mSecurityProperties.getCode().getSms().getLength());
        return new ValidateCode(code,mSecurityProperties.getCode().getSms().getExpireIn());
    }

    public SecurityProperties getSecurityProperties() {
        return mSecurityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        mSecurityProperties = securityProperties;
    }
}
