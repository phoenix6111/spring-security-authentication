package com.phoenix.security.core.validate.code.sms;

import com.phoenix.security.core.properties.SecurityConstants;
import com.phoenix.security.core.validate.code.ValidateCode;
import com.phoenix.security.core.validate.code.validateCodeProcessor.AbstractValidateCodeProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * User: phoenix
 * Date: 2018-03-22 15:26
 * Description:
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSender mSmsCodeSender;

    /**
     * 获取手机号并发送验证码
     * @param request
     * @param validateCode
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        //获取手机号
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);
        //发送手机验证码
        mSmsCodeSender.send(mobile,validateCode.getCode());
    }
}
