package com.phoenix.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 生成验证码的接口
 */
public interface ValidateCodeGenerator {

    /**
     * 生成验证码
     * @param request
     * @return
     */
    public ValidateCode generate(ServletWebRequest request);

}
