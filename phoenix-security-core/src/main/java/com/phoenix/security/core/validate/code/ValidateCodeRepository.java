package com.phoenix.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * User: sheng
 * Date: 2018-04-07 18:10
 * Description: 验证码存取器
 */
public interface ValidateCodeRepository {

    /**
     * 保存验证码
     * @param request
     * @param validateCode
     * @param validateCodeType
     */
    void save(ServletWebRequest request,ValidateCode validateCode,ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request,ValidateCodeType validateCodeType);

    /**
     * 移除验证码
     * @param request
     * @param validateCodeType
     */
    void remove(ServletWebRequest request,ValidateCodeType validateCodeType);
}
