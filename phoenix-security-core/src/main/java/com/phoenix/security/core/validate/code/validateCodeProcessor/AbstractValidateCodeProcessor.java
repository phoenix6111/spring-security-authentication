package com.phoenix.security.core.validate.code.validateCodeProcessor;

import com.phoenix.security.core.validate.code.ValidateCode;
import com.phoenix.security.core.validate.code.ValidateCodeException;
import com.phoenix.security.core.validate.code.ValidateCodeGenerator;
import com.phoenix.security.core.validate.code.ValidateCodeRepository;
import com.phoenix.security.core.validate.code.ValidateCodeType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * User: phoenix
 * Date: 2018-03-22 11:54
 * Description:
 */
public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor{

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String,ValidateCodeGenerator> validateCodeGenerators;

    /**
     * 验证码操作工具
     */
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * 创建校验码
     * @param request
     * @throws Exception
     */
    @Override
    public final void create(ServletWebRequest request) throws Exception {
        logger.info("create validate code");
        //生成验证码
        T validateCode = generate(request);
        logger.info(validateCode.toString());
        //保存验证码
        save(request,validateCode);
        //发送验证码
        send(request,validateCode);

    }

    /**
     * 发送验证码，由子类实现
     * @param request
     * @param validateCode
     */
    protected abstract void send(ServletWebRequest request,T validateCode) throws Exception;

    /**
     * 保存验证码
     * @param validateCode
     */
    private void save(ServletWebRequest request,T validateCode) {
        /**
         * 因为我们校验验证码时，只要校验验证码是否正确就行了，不需要把ImageCode时的BufferedImage或其它属性放进session，所以我们重新构造一个只包含
         * code和expireTime的ValidateCode放进session
         */
        validateCodeRepository.save(request,new ValidateCode(validateCode.getCode(),validateCode.getExpireTime()),getValidateCodeType(request));
    }

    /**
     * 生成校验码
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private T generate(ServletWebRequest request) {
        logger.info("generate validate code");
        String codeType = getValidateCodeType(request).toString().toLowerCase();
        String generatorName = codeType + ValidateCodeGenerator.class.getSimpleName();
        logger.info(generatorName);
        for(String key : validateCodeGenerators.keySet()) {
            logger.info("key == "+key);
        }

        ValidateCodeGenerator generator = validateCodeGenerators.get(generatorName);

        if (generator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }

        return (T) generator.generate(request);
    }


    /**
     *
     * 根据ValidateCodeProcessor的类名，获取ValidateCode的type
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(),"CodeProcessor");

        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 检验验证码
     * @param request
     */
    @Override
    @SuppressWarnings("unchecked")
    public void validate(ServletWebRequest request) {
        //获取验证码的类型
        ValidateCodeType validateCodeType = getValidateCodeType(request);
        //获取保存在系统中(session或redis)中保存的ValidateCode
        T codeInStore = (T) validateCodeRepository.get(request,validateCodeType);

        //request中验证码的值
        String codeInRequest;

        try {
            //获取request中验证码的值
            codeInRequest = ServletRequestUtils.getRequiredStringParameter(request.getRequest(),validateCodeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        //如果session中不存在验证码的值，则抛出异常
        if(codeInStore == null) {
            throw new ValidateCodeException(validateCodeType + "验证码不存在");
        }

        //判断验证码是否过期
        if(codeInStore.isExpired()) {
            //删除系统存储中的验证码
            validateCodeRepository.remove(request, validateCodeType);
            throw new ValidateCodeException(validateCodeType + "验证码已过期");
        }

        //如果request中不包含验证码，则抛出异常
        if(StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(validateCodeType + "验证码的值不能为空");
        }

        //判断输入的验证码是否正确
        if(!StringUtils.equalsIgnoreCase(codeInStore.getCode(),codeInRequest)) {
            throw new ValidateCodeException(validateCodeType + "验证码不匹配");
        }

        //最后将已经存在系统存储中的验证码删除
        validateCodeRepository.remove(request, validateCodeType);

    }
}
