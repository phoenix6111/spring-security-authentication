package com.phoenix.security.browser.validate.code;

import com.phoenix.security.core.validate.code.ValidateCode;
import com.phoenix.security.core.validate.code.ValidateCodeRepository;
import com.phoenix.security.core.validate.code.ValidateCodeType;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * User: sheng
 * Date: 2018-04-07 18:14
 * Description: 基于session的验证码存取器
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

    //验证码放入session时的前缀
    private static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    //操作session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 将ValidateCode保存进session
     * @param request
     * @param validateCode
     * @param validateCodeType
     */
    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        sessionStrategy.setAttribute(request,getSessionKey(request,validateCodeType),validateCode);
    }

    /**
     * 构建验证码放入session时的key
     * @param request
     * @param validateCodeType
     * @return
     */
    private String getSessionKey(ServletWebRequest request,ValidateCodeType validateCodeType) {
        return SESSION_KEY_PREFIX + validateCodeType.toString().toUpperCase();
    }

    /**
     * 从session中获取验证码
     * @param request
     * @param validateCodeType
     * @return
     */
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return (ValidateCode) sessionStrategy.getAttribute(request,getSessionKey(request,validateCodeType));

    }

    /**
     * 从session中删除验证码
     * @param request
     * @param validateCodeType
     */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        sessionStrategy.removeAttribute(request,getSessionKey(request,validateCodeType));
    }
}
